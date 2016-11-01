package com.core;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.gamelogic.Control;
import com.gamelogic.Player;
import com.gamelogic.Map;
import com.graphics.FPSMeasuring;
import com.graphics.OurGLSurfaceView;
import com.views.DropDownMenu;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Game implements Runnable {

    private Thread thread;
    private Map map;
    private Player player;
    private OurGLSurfaceView glSurfaceView;
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


    public Game(Context context) {
        Log.d("Game","Game greated");
        GUpdateable.game = this;
        glSurfaceView = new OurGLSurfaceView(context);

        this.context = context;

//        screenDrawer = new ScreenDrawer(context);
        objectsToUpdate = new ArrayList<>();
//        fpsDrawer = new FPSDrawer(context, screenDrawer);
        player = new Player();
        control = new Control(context, this);
//        collision = new Collision(map);

        // 30Hz clock
        handler = new Handler();
        handler.postDelayed(gameThread, 33);
        map = new Map(context);

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

    private final Runnable gameThread = new Runnable(){
        public void run(){
            try {
                update();
                handler.postDelayed(gameThread, 33);
//                Log.d("Game", "Testthread");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // Running gui thread
    @Override
    public void run() {

        while (isRunning) {
            if (!isPaused) {
                //update();
                glSurfaceView.requestRender();
            } else {
                try {
                    Log.d("Game", "Clock is Paused!");
                    // Slow loop down when paused to save CPU
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void gameStart() {
        isRunning = true;
        isPaused = false;
        fpsMeasuring.startFPS();
        //handler.post(gameThread);
    }

    public void gamePause() {
        isPaused = true;
        fpsMeasuring.stopFPS();
    }

    public void gameStop() {
        isRunning = false;
        fpsMeasuring.stopFPS();
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

    public OurGLSurfaceView getGameView() {
        return glSurfaceView;
    }

    public Control getControl() {
        return this.control;
    }

    public Map getMap() {
        return this.map;
    }

    public int getFPS() {
        return fpsMeasuring.latestFPS;
    }
}


