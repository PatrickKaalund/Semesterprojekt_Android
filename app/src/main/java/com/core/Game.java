package com.core;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;

import com.gamelogic.Player;
import com.gamelogic.Map;

import java.util.ArrayList;

public class Game implements Runnable {

    private Thread thread;
    private Map map;
    private Player player;
    private ScreenDrawer screenDrawer;
    private boolean isRunning;
    private Handler handler;
    private boolean isPaused;
    //    private UserInput userInput
    public ArrayList<GUpdateable> objectsToUpdate;
    private Context context;

    private FPSCounter fpsCounter = new FPSCounter();

    public Game(Context context) {
        GUpdateable.game = this;

        this.context = context;

        screenDrawer = new ScreenDrawer(context);
        objectsToUpdate = new ArrayList<>();
        map = new Map(context, screenDrawer);
        player = new Player(context, screenDrawer);

        // 30Hz clock
        handler = new Handler();

        gameStart();
        thread = new Thread(this);
        thread.start();

        fpsCounter.start();

//        userInput = new UserInput();
    }

    // Running thread with 30 Hz
    @Override
    public void run() {

        while (isRunning) {
            if (!isPaused) {
                update();
                screenDrawer.draw();
                fpsCounter.counter++;
            }

//            handler.postDelayed(this, 33);

//            try {
//                Thread.sleep(33);
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

    class FPSCounter extends Thread {
        public int counter = 0;

        // one time every second
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("FPS counter", "FPS: " + counter);
                counter = 0;
            }
        }
    }
}
