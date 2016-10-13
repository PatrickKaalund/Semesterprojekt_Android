package com.activities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.core.GameDrawable;
import com.core.GameStateHandler;
import com.core.GameLoop;
import com.example.patrickkaalund.semesterprojekt_android.R;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class InGame extends AppCompatActivity {

    private GameLoop gameLoop;
    public GameStateHandler gameStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameStateHandler = new GameStateHandler();

        GameDrawable gameDrawable = new GameDrawable(this, gameStateHandler);
        setContentView(gameDrawable);

        gameLoop = new GameLoop(gameStateHandler, gameDrawable);


    }

    @Override
    protected void onPause() {
        gameLoop.stopClock();
        super.onPause();
    }

    @Override
    protected void onPostResume() {
       gameLoop.startClock();
       super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        gameLoop.stopClock();
        super.onPause();
    }
}
