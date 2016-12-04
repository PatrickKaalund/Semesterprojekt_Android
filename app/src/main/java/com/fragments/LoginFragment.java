package com.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.network.Firebase.LoginHandler;


public class LoginFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;
    private LoginHandler loginHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        this.view = view;

        loginHandler = new LoginHandler();

        TextView nextButton = (TextView) view.findViewById(R.id.buttonPlay);

        nextButton.setOnClickListener(this);

        audioPlayer = new AudioPlayer(view.getContext());

        view.bringToFront();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                Log.d("Main", "Clicked");
                audioPlayer.playAudioFromRaw(R.raw.click);
                loginHandler.login(this, "TestPerson");
                break;
//                v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));

        }
    }

    // Used by LoginHandler
    public void loggedIn(boolean succeeded) {
        if (succeeded) {
            // Logged in
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            preferences.edit().putBoolean("logged_in", true);

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .remove(this)
                    // Close keyboard fragment
                    .remove(getFragmentManager().findFragmentById(R.id.start_keyboard_fragment_holder))
                    .add(R.id.activity_main_menu, new ButtonMainFragment())
                    .commit();
        } else {
            String errorMessage = "Already a player with this name. Try again";
            Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
            Log.e("LoginFragment", errorMessage);
        }
    }
}
