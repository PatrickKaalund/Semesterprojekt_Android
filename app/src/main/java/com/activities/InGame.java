package com.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.core.Game;



public class InGame extends AppCompatActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = new Game(this);
        setContentView(game.getScreenDrawer());
    }

    @Override
    protected void onPause() {
        game.stop();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        game.start();
       super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        game.stop();
        super.onPause();
    }
}
