package com.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.core.Game;
import com.example.patrickkaalund.semesterprojekt_android.R;


public class InGame extends AppCompatActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(game.getScreenDrawer());
        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.activity_in_game, null),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onPause() {
//        game.stop();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
//        game.start();
       super.onPostResume();
    }

    @Override
    protected void onDestroy() {
//        game.stop();
        super.onPause();
    }
}
