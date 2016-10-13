package core;

import android.os.Handler;
import android.util.Log;

public class GameLoop {
    private Handler handler;

    // 30Hz clock
    public GameLoop() {
        handler = new Handler();
        handler.postDelayed(clock, 33);
    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            Log.d("GameLoop", "Clock ticked!");
            handler.postDelayed(this, 33);
        }
    };
}
