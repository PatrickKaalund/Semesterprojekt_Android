package com.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.graphics.OurGLSurfaceView;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.HighScoreHandler;

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
    private OurGLSurfaceView glSurfaceView;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            shotsFired = bundle.getInt("shots");
            kills = bundle.getInt("kills");
            hits = bundle.getInt("hits");
        }

        // Publish high-score
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        HighScoreHandler highScoreHandler = new HighScoreHandler();
        highScoreHandler.publishHighScore(preferences.getString("player", "DEFAULT"), kills);

        // Inflate view
        View view = inflater.inflate(R.layout.fragment_end_game, container, false);
        this.view = view;

        // Create audioplayer
        audioPlayer = new AudioPlayer(view.getContext());

        // Create new glSurfaceView
        glSurfaceView = new OurGLSurfaceView(getActivity().getApplicationContext());

        // Inflate glSurfaceView on top of running activity
        ((Activity) view.getContext()).getWindow().addContentView(glSurfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        glSurfaceView.setZOrderOnTop(true);

        // Set onClickLIsteners
        TextView nextButton = (TextView) view.findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);

        // Bring view to front
        view.bringToFront();

        // Get displaymetrics
        displayMetrics = view.getResources().getDisplayMetrics();

        // Create spritefactory
        endScreenFactory = new SpriteEntityFactory(R.drawable.numbers_fps, 100, 100, 11, 1, new PointF(0, 0));

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create & start drawers
        killDrawers = new ArrayList<>();
        hitsDrawers = new ArrayList<>();
        shotsDrawers = new ArrayList<>();

        startDrawers(killDrawers, 0);
        startDrawers(shotsDrawers, 110);
        startDrawers(hitsDrawers, 220);

        // Start drawing thread
        new Thread(background).start();
    }


    public void startDrawers(ArrayList<Entity> drawers, int yOffset) {

        // Place each drawer on same x-coordinate with 60 pixels in between
        // Use yOffset as y-offset :)
        for (int i = 0; i < 4; i++) {
            Entity drawer = endScreenFactory.createEntity();
            drawer.placeAt(displayMetrics.widthPixels/2 + 200 - (60 * i), displayMetrics.heightPixels/2 + 90 - yOffset);
            drawer.setCurrentSprite(0);
            drawers.add(drawer);
        }
    }


    private Runnable background = new Runnable() {

        // Wait for 600millis and draw numbers
        private void draw(ArrayList<Entity> drawers, int size) throws InterruptedException {
            Thread.sleep(600);

            // Get length of number to draw
            String sizeString =  Integer.toString(size);
            if (size < 9999 && size > 0) {

                // Draw each number separately
                for (int i = 0; i < sizeString.length(); i++) {
                    // get next digit in string  - convert from ASCII char
                    int sprite = sizeString.charAt(sizeString.length() - (i + 1)) - 48;

                    for (int j = 0; j < sprite; j++) {
                        drawers.get(i).setCurrentSprite(j + 1);

                        // Request redraw and play click-sound then wait for 300millis
                        audioPlayer.playAudioFromRaw(R.raw.click);
                        glSurfaceView.requestRender();
                        Thread.sleep(350);
                    }
                }
                // Play heart-beat
                audioPlayer.playAudioFromRaw(R.raw.heartbeat);
            }
        }

        @Override
        public void run() {
            // Request drawing from background thread
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                Log.d("Main", "Clicked");
                // Run animation now to match time with fragment removal
                v.startAnimation(AnimationUtils.loadAnimation(view.getContext(), R.anim.view_clicked));

                // Delete sprite factory and remove glSurfaceView
                endScreenFactory.delete();
                glSurfaceView.setVisibility(View.INVISIBLE);

                // Play click
                audioPlayer.playAudioFromRaw(R.raw.click);

                // Remove this fragment from stack and put high-score fragment on stack
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .remove(this)
                        .add(R.id.fragment_endgame_holder, new HighScoreFragment())
                        .commit();

                break;
        }
    }
}