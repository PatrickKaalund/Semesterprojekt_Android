package com.core;


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.support.design.widget.FloatingActionButton;

import com.gamelogic.Collision;
import com.gamelogic.Control;
import com.gamelogic.Player;
import com.gamelogic.Map;
import com.graphics.OurGLSurfaceView;
import com.views.DropDownMenu;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Game implements Runnable {

    private Thread thread;
    private Map map;
    private Player player;
//    private ScreenDrawer screenDrawer;
    private boolean isRunning;
    private Handler handler;
    private boolean isPaused;
    private Control control;
    private FPSDrawer fpsDrawer;
    private int latestFPS = 0;
//    private Collision collision;

    public ArrayList<GUpdateable> objectsToUpdate;
    private Context context;

    private FPSMeasuring fpsMeasuring = new FPSMeasuring();
    private OurGLSurfaceView gl;

    public Game(Context context) {
        GUpdateable.game = this;

        gl = new OurGLSurfaceView(context);

        this.context = context;

//        screenDrawer = new ScreenDrawer(context);
        objectsToUpdate = new ArrayList<>();
//        map = new Map(context, screenDrawer);
//        fpsDrawer = new FPSDrawer(context, screenDrawer);
//        player = new Player(context, screenDrawer);
        control = new Control(context, this);
//        collision = new Collision(map);

        // 30Hz clock
        handler = new Handler();

        gameStart();
        thread = new Thread(this);
        thread.start();

        fpsMeasuring.start();
    }

    public void setJoystick(JoystickView joystickView) {
        control.setJoystick(joystickView);
    }

    public void setShootButton(FloatingActionButton shootButton) {
        control.setShootButton(shootButton);
    }

    public void setInventoryButton(DropDownMenu dropDownMenu) {
        control.setInventoryButton(dropDownMenu);
    }

    // Running thread with 30 Hz
    @Override
    public void run() {

        while (isRunning) {
            if (!isPaused) {
                update();
//                screenDrawer.draw();
                fpsMeasuring.counter++;
            } else { try {
                    Log.d("Game", "Clock is Paused!");
                    // Slow loop down when paused to save CPU
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
//        control.read();
    }

//    public ScreenDrawer getScreenDrawer() {
//        return this.screenDrawer;
//    }

    public OurGLSurfaceView getGlSurfaceView(){
        return gl;
    }

    public Control getControl() {
        return this.control;
    }
    public Map getMap() {
        return this.map;
    }
    public int getFPS() {return this.latestFPS; }


    // for debugging
    class FPSMeasuring extends Thread {
        public int counter = 0;

        // one time every second
        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Log.d("FPS counter", "FPS: " + counter);
                latestFPS = counter;
                counter = 0;
            }
        }
    }
}
