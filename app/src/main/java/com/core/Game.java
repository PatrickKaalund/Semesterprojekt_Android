package com.core;


import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.gamelogic.Control;
import com.gamelogic.Player;
import com.gamelogic.Map;
import com.graphics.FPSDrawer;
import com.graphics.FPSMeasuring;
import com.graphics.OurGLSurfaceView;
import com.network.Firebase.NetworkHandler;
import com.views.DropDownMenu;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Game implements Runnable {

    private Thread thread;
    public Map map;
    private Player player;
    private NetworkHandler networkHandler;

    //    private Enemy enemy;
    private OurGLSurfaceView glSurfaceView;
    //    private ScreenDrawer screenDrawer;
    private boolean isRunning;
    private Handler handler;
    private boolean isPaused;
    private Control control;

    private int latestFPS = 0;
//    private Collision collision;

    public ArrayList<GUpdateable> objectsToUpdate;
    private Context context;

    private FPSMeasuring fpsMeasuring;


    public Game(Context context) {
        Log.d("Game","Game created");
        GUpdateable.game = this;
        glSurfaceView = new OurGLSurfaceView(context);

        this.context = context;

        objectsToUpdate = new ArrayList<>();
//        fpsDrawer = new FPSDrawer(context, screenDrawer);

        networkHandler = new NetworkHandler();
//        networkHandler.addPlayerListener(this);

        player = new Player(networkHandler);
        control = new Control(context, this);
        map = new Map(context, networkHandler);

        fpsMeasuring = new FPSMeasuring(context);

        gameStart();
        thread = new Thread(this);
        thread.start();

//        Thread thread2 = new Thread(renderThread);
//        thread2.start();

        fpsMeasuring.start();

        update();
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

    // Running gui thread
    @Override
    public void run() {

        while (isRunning) {
            if (!isPaused) {
//                handler.postDelayed(this, 33);
                update();
//                glSurfaceView.requestRender();

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
    }

    // Getters
    public OurGLSurfaceView getGameView() {
        return glSurfaceView;
    }
    public Control getControl() {
        return this.control;
    }
    public int getFPS() {
        return fpsMeasuring.latestFPS;
    }
    public Player getPlayer(){ return this.player; }
}


