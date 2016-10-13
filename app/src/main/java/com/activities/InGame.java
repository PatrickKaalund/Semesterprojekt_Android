package com.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.core.GameDrawable;
import com.core.GameLoop;
import com.example.patrickkaalund.semesterprojekt_android.R;

public class InGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GameDrawable gameDrawable = new GameDrawable(this);
        //GrafikView2 grafikView = new GrafikView2(this);
        //gameDrawable.setBackgroundResource(R.drawable.logo);
        setContentView(gameDrawable);

        GameLoop gameLoop = new GameLoop(gameDrawable); // Start GameLoop (debug)
    }
}
