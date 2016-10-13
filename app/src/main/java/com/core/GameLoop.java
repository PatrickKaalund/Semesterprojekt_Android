package com.core;

import android.graphics.Canvas;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class GameLoop {
    private Handler handler;
    private GameStateHandler gameStateHandler;
    private GameDrawable gameDrawable;
    private Canvas canvas;
    private boolean isPaused;

    public GameLoop(GameDrawable gameDrawable) {
        this.gameDrawable = gameDrawable;
        handler = new Handler();
        gameStateHandler = new GameStateHandler();
        canvas = new Canvas();
        startClock();
    }

    public void stopClock() {
        isPaused = true;
    }

    public void startClock() {
        isPaused = false;
        handler.postDelayed(clock, 33);
    }

    // 30Hz clock
    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                handler.postDelayed(this, 33);
                Log.d("GameLoop", "Clock ticked!");
                gameStateHandler.update();
            }

            // updating canvas
            //gameStateHandler.draw(canvas);
            // draw canvas
            //gameDrawable.draw(canvas);
        }
    };
}
