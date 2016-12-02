package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.activities.InGame;
import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;


public class MultiplayerFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;
    public int numberOfPlayers = 2;
    public int scrollYpos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        this.view = view;

        TextView playButton = (TextView) view.findViewById(R.id.buttonPlayM);

        playButton.setOnClickListener(this);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(5);

//        enableNumberPickerManualEditing(numberPicker, false);

        String[] test = { "2 players", "3 players", "4 players", "5 players" };

        EditText tv1 = (EditText)numberPicker.getChildAt(0);
        tv1.setTextSize(30);

        numberPicker.setDisplayedValues(test);
        numberPicker.setSoundEffectsEnabled(true);

        audioPlayer = new AudioPlayer(view.getContext());

        view.bringToFront();

        return view;
    }
//
//    public void enableNumberPickerManualEditing(NumberPicker numPicker,
//                                                boolean enable) {
//        int childCount = numPicker.getChildCount();
//
//        for (int i = 0; i < childCount; i++) {
//            View childView = numPicker.getChildAt(i);
//
//            if (childView instanceof EditText) {
//                EditText et = (EditText) childView;
//                et.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.multiplayer_2_players));
//                et.setTextSize(32);
//                et.setFocusable(enable);
//                return;
//            }
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlayM:
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

