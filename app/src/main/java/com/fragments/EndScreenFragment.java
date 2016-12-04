package com.fragments;

import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;


public class EndScreenFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AudioPlayer audioPlayer;
    private Entity killDrawer;
    private Entity hitsDrawer;
    private Entity shotsDrawer;
    private Entity ratioDrawer;
    private int shotsFired;
    private int hits;
    private int kills;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shotsFired = bundle.getInt("shots");
            kills = bundle.getInt("kills");
            hits = bundle.getInt("hits");
        }

        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        this.view = view;

        TextView nextButton = (TextView) view.findViewById(R.id.buttonNext);

        nextButton.setOnClickListener(this);

        audioPlayer = new AudioPlayer(view.getContext());

        view.bringToFront();

        DisplayMetrics displayMetrics = view.getResources().getDisplayMetrics();
        SpriteEntityFactory endScreenFactory = new SpriteEntityFactory(R.drawable.numbers_fps, 120, 120, 11, 1, new PointF(0, 0));
        killDrawer = endScreenFactory.createEntity();

        killDrawer.placeAt(displayMetrics.widthPixels - 300, displayMetrics.heightPixels - 738);
        killDrawer.setCurrentSprite(0);

        shotsDrawer = endScreenFactory.createEntity();
        shotsDrawer.placeAt(displayMetrics.widthPixels - 300, displayMetrics.heightPixels - 848);

        hitsDrawer = endScreenFactory.createEntity();
        hitsDrawer.placeAt(displayMetrics.widthPixels - 300, displayMetrics.heightPixels - 958);
        hitsDrawer.setCurrentSprite(0);

        ratioDrawer = endScreenFactory.createEntity();
        ratioDrawer.placeAt(displayMetrics.widthPixels - 300, displayMetrics.heightPixels - 1068);
        ratioDrawer.setCurrentSprite(0);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread( background ).start();
    }

    private Runnable background = new Runnable() {
        @Override
        public void run() {
            try {
                if (kills < 9999) {
                    for (int i = 0; i < (kills % 10); i++) {
                        killDrawer.setCurrentSprite(i + 1);
                        audioPlayer.playAudioFromRaw(R.raw.click);
                        Thread.sleep(300);
                    }
                }

                if (shotsFired < 9999) {
                    for (int i = 0; i < (shotsFired % 10); i++) {
                        shotsDrawer.setCurrentSprite(i + 1);
                        audioPlayer.playAudioFromRaw(R.raw.click);
                        Thread.sleep(300);
                    }
                }

                if (hits < 9999) {
                    for (int i = 0; i < (hits % 10); i++) {
                        hitsDrawer.setCurrentSprite(i + 1);
                        audioPlayer.playAudioFromRaw(R.raw.click);
                        Thread.sleep(300);
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


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
                        .add(R.id.fragment_endgame_holder, new HighScoreFragment())
                        .commit();
                break;

        }
    }
}
