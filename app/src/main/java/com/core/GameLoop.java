package com.core;

import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;

public class GameLoop {
    private Handler handler;
    private GameStateHandler gameStateHandler;
    private GameDrawable gameDrawable;
    private Canvas canvas;

    // 30Hz clock
    public GameLoop(GameDrawable gameDrawable) {
        handler = new Handler();
        handler.postDelayed(clock, 33);

        gameStateHandler = new GameStateHandler();
        this.gameDrawable = gameDrawable;

        canvas = new Canvas();
    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            Log.d("GameLoop", "Clock ticked!");
            handler.postDelayed(this, 33);

            gameStateHandler.update();
            // updating canvas
            //gameStateHandler.draw(canvas);
            // draw canvas
            //gameDrawable.draw(canvas);
        }
    };
}
