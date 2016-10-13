package com.core;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;

public class GameLoop {
    private Handler handler;
    private GameStateHandler gameStateHandler;
    private GameDrawable gameDrawable;
    private boolean isPaused;

    // 30Hz clock
    public GameLoop(GameStateHandler gameStateHandler, GameDrawable gameDrawable) {
        handler = new Handler();
        handler.postDelayed(clock, 33);

        this.gameStateHandler = gameStateHandler;
        this.gameDrawable = gameDrawable;

        startClock();
    }

    public void stopClock() {
        isPaused = true;
    }

    public void startClock() {
        isPaused = false;
        handler.postDelayed(clock, 33);
    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                handler.postDelayed(this, 33);
                //Log.d("GameLoop", "Clock ticked!");

                // updating game logic
                gameStateHandler.update();

                // draw canvas
                gameDrawable.invalidate();
            }
        }
    };
}
