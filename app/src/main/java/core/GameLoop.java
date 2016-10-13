package core;

import android.os.Handler;
import android.util.Log;

import models.Player;

public class GameLoop {
    private Handler handler;
    private GameStateHandler gameStateHandler;

    // 30Hz clock
    public GameLoop() {
        handler = new Handler();
        handler.postDelayed(clock, 33);

        gameStateHandler = new GameStateHandler();

    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            Log.d("GameLoop", "Clock ticked!");
            handler.postDelayed(this, 33);

            gameStateHandler.update();
        }
    };
}
