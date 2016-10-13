package com.activities;

import android.app.Activity;
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

public class InGame extends AppCompatActivity {

    public GameStateHandler gameStateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameStateHandler = new GameStateHandler();

        GameDrawable gameDrawable = new GameDrawable(this, gameStateHandler);
        setContentView(gameDrawable);

        GameLoop gameLoop = new GameLoop(gameStateHandler, gameDrawable);
    }
}
