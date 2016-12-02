package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.activities.InGame;
import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;


public class MultiplayerFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        this.view = view;

        TextView playButton = (TextView) view.findViewById(R.id.buttonPlayM);

        playButton.setOnClickListener(this);

        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPickerMultiplayer);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                Log.e("MultiplayerFragment", "Selected Number : " + newVal);
            }
        });

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
                Log.d("MultiplayerFragment", "Start multiplayer clicked!");
                v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));
                audioPlayer.playAudioFromRaw(R.raw.click);

                // Bring progressBar to top
                getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.progressBar).bringToFront();
                getActivity().findViewById(R.id.progressBar).invalidate();

                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.view_clicked));

                Intent play = new Intent(getContext(), InGame.class);
                startActivity(play);

                // Returning to main menu, when pressing back from in game multiplayer
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .remove(this)
                        .add(R.id.activity_main_menu, new ButtonMainFragment())
                        .commit();
                break;
        }
    }
}
