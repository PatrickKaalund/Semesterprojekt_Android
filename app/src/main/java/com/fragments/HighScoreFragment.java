package com.fragments;

import android.app.Activity;
import android.graphics.Point;
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
    private int yOffset;

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
        highScoreFactory = new SpriteEntityFactory(R.drawable.numbers_letters, 70, 70, 11, 4, new PointF(0, 0));

        view.bringToFront();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        highScoreFactory.delete();
        glSurfaceView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        this.yOffset = this.displayMetrics.heightPixels - location[1] - 220;
        Log.e("HighScoreFragment", "Y offset: " + this.yOffset);
//        Log.e("HighScoreFragment", "Width: " + view.getTop()/2 + ". Height: " + view.getRight()/2);
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

        int highScoreElement = 0;
        while (queue.peek() != null) {

            HighScoreObject highScoreObject = queue.poll();
            String name = highScoreObject.name.toUpperCase();
            String points = Integer.toString(highScoreObject.value).toUpperCase();

            drawHighScoreListElement(name, points, highScoreElement);

            // Draw top 4 on high score list
            if (++highScoreElement == 4) {
                return;
            }
        }
    }

    private void drawHighScoreListElement(String name, String points, int indexHeight) {
        if (indexHeight == 0) {
            drawString("RANK", new Point(displayMetrics.widthPixels / 2 - 390, yOffset));
            drawString("SCORE", new Point(displayMetrics.widthPixels / 2 - 140, yOffset));
            drawString("NAME", new Point(displayMetrics.widthPixels / 2 + 180, yOffset));
        }

        Point placementOnScreen = new Point(displayMetrics.widthPixels / 2 - 370, yOffset - 100 - (indexHeight * 80));

        switch (indexHeight) {
            case 0:
                drawString("1ST", placementOnScreen);
                break;
            case 1:
                drawString("2ND", placementOnScreen);
                break;
            case 2:
                drawString("3RD", placementOnScreen);
                break;
            // default case index > 2:
            default:
                drawString((indexHeight + 1) + "TH", placementOnScreen);
                break;
        }

        // Fill up points with zeroes in front - 4 ciffers
        int pointsWithZeroes = Integer.parseInt(points);
        if (pointsWithZeroes < 10) {
            points = "000" + points;
        } else if (pointsWithZeroes < 100) {
            points = "00" + points;
        } else if (pointsWithZeroes < 1000) {
            points = "0" + points;
        }
        drawString(points, new Point(displayMetrics.widthPixels / 2 - 120, placementOnScreen.y));

        // Trim name
        if (name.length() > 5) {
            name = name.substring(0, 5);
        }

        drawString(name, new Point(displayMetrics.widthPixels / 2 + 180, placementOnScreen.y));
    }

    private void drawString(String text, Point placementOnScreen) {

        // Draw each number separately
        for (int i = 0; i < text.length(); i++) {
            Entity drawer = highScoreFactory.createEntity();
            drawer.placeAt(placementOnScreen.x + (55 * i), placementOnScreen.y);
            drawer.setCurrentSprite(0);
//            Log.e("HighScoreFragment", "Added drawer");

            // Convert from ASCII to spritesheet indexes
            int spriteSheetOffset = 55;
            // Adjust for letters
            int sprite = text.charAt(i) - spriteSheetOffset;

            // Adjust for numbers
            if (sprite >= -7 && sprite < 4) {
                sprite += 7;
            }
            // Unknown sprite - Draw space
            else if (sprite > 35 || sprite < 10) {
                sprite = 41;
            }

            drawer.setCurrentSprite(sprite);

//            Log.e("HighScoreFragment", "Shown sprite number: " + sprite + ". Char at: " + text.charAt(i));
        }
        glSurfaceView.requestRender();
    }
}
