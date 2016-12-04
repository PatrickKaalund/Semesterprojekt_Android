package com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;


public class HighScoreFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_high_score, container, false);
        this.view = view;

        TextView nextButton = (TextView) view.findViewById(R.id.buttonNext);

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
            case R.id.buttonNext:
                Log.d("Main", "Clicked");

                v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));

                audioPlayer.playAudioFromRaw(R.raw.click);

                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .remove(this)
                        .commit();

                Activity a = (Activity) view.getContext();
                a.finish();

                break;
        }
    }
}
