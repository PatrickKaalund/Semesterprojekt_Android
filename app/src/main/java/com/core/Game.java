package com.core;


import android.content.Context;
import android.graphics.Canvas;

import com.gamelogic.Player;
import com.gamelogic.Map;
import java.util.ArrayList;

public class Game implements Runnable {

    private Thread thread;
    private Map map;
    private Player player;
    private ScreenDrawer screenDrawer;
    private boolean isRunning;
    //    private Handler handler;
    private boolean isPaused;
    //    private UserInput userInput
    public ArrayList<GUpdateable> objectsToUpdate;
    private Context context;

    public Game(Context context) {
        GUpdateable.game = this;

        this.context = context;

        screenDrawer = new ScreenDrawer(context);
        objectsToUpdate = new ArrayList<>();
        map = new Map(context, screenDrawer);
        player = new Player(context, screenDrawer);

        // 30Hz clock
//        handler = new Handler();

        gameStart();
        thread = new Thread(this);
        thread.start();

//        userInput = new UserInput();
    }

    // Running thread with 30 Hz
    @Override
    public void run() {
        Canvas canvas = null;

        while (isRunning) {
            if (!isPaused) {
                update();
                screenDrawer.draw();
//                update();
//                try {
//                    canvas = screenDrawer.getHolder().lockCanvas(null);
//                    synchronized (screenDrawer.getHolder()) {
//                        screenDrawer.postInvalidate();
//                    }
//                } finally {
//                    if (canvas != null) {
////                   Log.d("Game", "Unlocking canvas!");
//                        screenDrawer.getHolder().unlockCanvasAndPost(canvas);
//                    } else {
//                        Log.d("Game", "Canvas is null!");
//                    }
//                }
            }
//            try {
//                this.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    public void gameStart() {
        isRunning = true;
        isPaused = false;
        //this.run();
    }

    public void gamePause() {
        isPaused = true;
    }

    public void gameStop() {
        isRunning = false;
    }

    public void update() {
        for (GUpdateable updateable : objectsToUpdate) {
            updateable.update();
        }
//        userInput.read();
    }

    public ScreenDrawer getScreenDrawer() {
        return screenDrawer;
    }

}
