package com.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.activities.InGame;
import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;

public class MultiplayerFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;
    private int numberOfPlayers;
    private boolean isInMultiplayerFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        this.view = view;

        TextView playButton = (TextView) view.findViewById(R.id.buttonPlayM);

        playButton.setOnClickListener(this);

        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(2);
        numberPicker.setMaxValue(5);

        enableNumberPickerManualEditing(numberPicker, false);

        updateView(numberPicker, 2);

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
                    updateView(picker, newVal);
            }
        });

        numberPicker.setWrapSelectorWheel(true);

        // Empty - showing drawables inside updateView instead. Must be set in order to not showing default numbers
        String[] test = { " ", " ", " ", " " };
        numberPicker.setDisplayedValues(test);

        numberPicker.setSoundEffectsEnabled(true);

        audioPlayer = new AudioPlayer(view.getContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view.isShown()){
                    Toast.makeText(getActivity(), "Scroll to change total players", Toast.LENGTH_SHORT).show();
                }
            }
        }, 1500);

        isInMultiplayerFragment = true;

        view.bringToFront();

        return view;
    }


    private void updateView(View view, int newVal){
//        Log.d("MultiplayerFragment", "Updating! New val: " + newVal);

        this.numberOfPlayers = newVal;

        switch (newVal){
            case 2:
                view.setBackground(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.multiplayer_2_player));
                break;
            case 3:
                view.setBackground(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.multiplayer_3_player));
                break;
            case 4:
                view.setBackground(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.multiplayer_4_player));
                break;
            case 5:
                view.setBackground(ContextCompat.getDrawable(this.getContext().getApplicationContext(), R.drawable.multiplayer_5_player));
                break;
            default:
                Log.e("MultiplayerFragment", "In default mode!!");
        }
    }


    public void enableNumberPickerManualEditing(NumberPicker numPicker,
                                                boolean enable) {
        int childCount = numPicker.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View childView = numPicker.getChildAt(i);

            if (childView instanceof EditText) {
                EditText et = (EditText) childView;
                et.setFocusable(enable);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlayM:
                Log.d("MultiplayerFragment", "Start multiplayer clicked! Number of players: " + this.numberOfPlayers);
                if(this.numberOfPlayers > 2){
                    Toast.makeText(getActivity(), "Max 2 players at the moment", Toast.LENGTH_SHORT).show();
                    break;
                }
                isInMultiplayerFragment = false;

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

