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

    // 30Hz clock
    public GameLoop(GameStateHandler gameStateHandler, GameDrawable gameDrawable) {
        handler = new Handler();
        handler.postDelayed(clock, 33);

        this.gameStateHandler = gameStateHandler;
        this.gameDrawable = gameDrawable;
    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            Log.d("GameLoop", "Clock ticked!");
            handler.postDelayed(this, 33);

            // updating game logic
            gameStateHandler.update();

            // draw canvas
            gameDrawable.invalidate();
        }
    };
}
