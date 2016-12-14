package com.fragments;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.activities.InGame;
import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.OurGLSurfaceView;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.HighScoreHandler;
import com.network.Firebase.HighScoreObject;
import com.network.Firebase.NetworkHandler;

import java.util.ArrayList;
import java.util.PriorityQueue;


public class HighScoreFragment extends Fragment implements View.OnClickListener {

    View view;
    private AudioPlayer audioPlayer;
    private HighScoreHandler highScoreHandler;
    private OurGLSurfaceView glSurfaceView;
    private SpriteEntityFactory highScoreFactory;
    private DisplayMetrics displayMetrics;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_high_score, container, false);
        this.view = view;

        // Create new glSurfaceView
        glSurfaceView = new OurGLSurfaceView(getActivity().getApplicationContext());

        // Bring progressBar to top
        getActivity().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.progressBar).bringToFront();
        getActivity().findViewById(R.id.progressBar).invalidate();

        displayMetrics = view.getResources().getDisplayMetrics();

        highScoreHandler = new HighScoreHandler();
        highScoreHandler.requestHighScoreList(this);

        TextView mainMenuButton = (TextView) view.findViewById(R.id.buttonMainMenu);

        mainMenuButton.setOnClickListener(this);

        audioPlayer = new AudioPlayer(view.getContext());

        // Create spritefactory
        highScoreFactory = new SpriteEntityFactory(R.drawable.letters_red, 80, 80, 16, 2, new PointF(0, 0));

        view.bringToFront();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        glSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMainMenu:
                Log.d("Main", "Clicked");

                v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));

                audioPlayer.playAudioFromRaw(R.raw.click);

                glSurfaceView.setVisibility(View.INVISIBLE);

                if (getActivity() instanceof InGame) {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .remove(this)
                            .commit();

                    // Change activity to main menu
                    Activity a = (Activity) view.getContext();
                    a.finish();

                } else {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                            .remove(this)
                            .add(R.id.activity_main_menu, new ButtonMainFragment())
                            .commit();
                }
                break;
        }
    }

    public void fillHighScore(PriorityQueue<HighScoreObject> queue) {
//        Log.e("HighScoreFragment", "Info: " + info.toString());

        // Close progressbar
        getActivity().findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);

        // Inflate glSurfaceView on top of running activity
        ((Activity) view.getContext()).getWindow().addContentView(glSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        glSurfaceView.setZOrderOnTop(true);

        int counter = 0;
        while (queue.peek() != null) {

            HighScoreObject highScoreObject = queue.poll();
            String name = highScoreObject.name.toUpperCase();

            drawHighScoreListElement(name, counter);
//            (highScoreObject.name + " : " + highScoreObject.value);

            // Draw top 4 on high score list
            if (++counter == 4) {
                return;
            }
        }
    }

    private void drawHighScoreListElement(String text, int indexHeight) {
        if (indexHeight == 0) {
            drawString("RANK", new Point(displayMetrics.widthPixels/2 - 300, displayMetrics.heightPixels/2 + 300));
        }

        // Draw each letter separately
//        drawString(text, new Point());
    }

    private void drawString(String text, Point placementOnScreen) {

        // Draw each number separately
        for (int i = 0; i < text.length(); i++) {
            Entity drawer = highScoreFactory.createEntity();
            drawer.placeAt(placementOnScreen.x + (60 * i), placementOnScreen.y);
            drawer.setCurrentSprite(0);
//            Log.e("HighScoreFragment", "Added drawer");

            // Convert from ASCII to sprite
            int sprite = text.charAt(i) - 65;
//            Log.e("HighScoreFragment", "Shown sprite number: " + sprite + ". Char at: " + text.charAt(i));

            if (sprite >= 0 && sprite < 26) {
                drawer.setCurrentSprite(sprite);
            } else if (sprite == -33) {
                drawer.setCurrentSprite(31);
            } // Space
            else {
                Log.e("HighScoreFragment", "Unvalid sprite number: " + sprite + ". Char at: " + text.charAt(i));
                drawer.setCurrentSprite(26);
            }
        }
        audioPlayer.playAudioFromRaw(R.raw.click);
        glSurfaceView.requestRender();
    }
}
