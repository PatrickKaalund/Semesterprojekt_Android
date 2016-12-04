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

import java.util.ArrayList;


public class EndScreenFragment extends Fragment implements View.OnClickListener {

    private View view;
    private AudioPlayer audioPlayer;

    private int shotsFired;
    private int hits;
    private int kills;

    private ArrayList<Entity> killDrawers;
    private ArrayList<Entity> hitsDrawers;
    private ArrayList<Entity> shotsDrawers;
    private SpriteEntityFactory endScreenFactory;
    private DisplayMetrics displayMetrics;

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

        displayMetrics = view.getResources().getDisplayMetrics();
        endScreenFactory = new SpriteEntityFactory(R.drawable.numbers_fps, 120, 120, 11, 1, new PointF(0, 0));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        killDrawers = new ArrayList<>();
        hitsDrawers = new ArrayList<>();
        shotsDrawers = new ArrayList<>();

        startDrawers(killDrawers, 0);
        startDrawers(shotsDrawers, 110);
        startDrawers(hitsDrawers, 220);

        new Thread(background).start();
    }


    public void startDrawers(ArrayList<Entity> drawers, int yOffset) {

        for (int i = 0; i < 4; i++) {
            Entity drawer = endScreenFactory.createEntity();
            drawer.placeAt(displayMetrics.widthPixels - 300 - (60 * i), displayMetrics.heightPixels - 738 - yOffset);
            drawer.setCurrentSprite(0);
            drawers.add(drawer);
        }
    }


    private Runnable background = new Runnable() {

        private void draw(ArrayList<Entity> drawers, int size) throws InterruptedException {
            String sizeString =  Integer.toString(size);
            if (size < 9999 && size > 0) {

                for (int i = 0; i < sizeString.length(); i++) {
                    // get next digit in string  - convert from ASCII char
                    int sprite = sizeString.charAt(sizeString.length() - (i + 1)) - 48;
                    Log.d("EndScreen_background", "Read: " + sprite);

                    for (int j = 0; j < sprite; j++) {
                        drawers.get(i).setCurrentSprite(j + 1);

                        audioPlayer.playAudioFromRaw(R.raw.click);

                        Thread.sleep(350);
                    }
                }
            }
        }

        @Override
        public void run() {
            try {
                draw(killDrawers, kills);
                draw(shotsDrawers, shotsFired);
                draw(hitsDrawers, hits);
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