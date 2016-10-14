package com.core;


import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;

import com.gamelogic.Control;
import com.gamelogic.Player;
import com.gamelogic.Map;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Game {

    private Map map;
    private Player player;
    private ScreenDrawer screenDrawer;
    private Handler handler;
    private boolean isPaused;
    private Control userInput;

    public ArrayList<GUpdateable> objectsToUpdate;

    public Game(Context context) {
        GUpdateable.game = this;
        objectsToUpdate = new ArrayList<>();
        screenDrawer = new ScreenDrawer(context);
        map = new Map(context, screenDrawer);
        player = new Player(context, screenDrawer);
        userInput = new Control(context);
        // 30Hz clock
        handler = new Handler();
        start();
    }

    public void start() {
        isPaused = false;
        handler.postDelayed(clock, 33);
    }

    public void setJoystick(JoystickView joystickView) {
        userInput.setJoystick(joystickView);
    }

    public void setShootButton(FloatingActionButton shootButton) {
        userInput.setShootButton(shootButton);
    }

    public void stop() {
        isPaused = true;
    }

    private Runnable clock = new Runnable() {
        @Override
        public void run() {
            if (!isPaused) {
                handler.postDelayed(this, 33);
                update();
                //Log.d("Game", "Clock ticked!");
            }
        }
    };


    public void update() {
        for (GUpdateable updateable : objectsToUpdate) {
            updateable.update();
        }
//        userInput.read();
        //map.update();
        //player.update();
        screenDrawer.invalidate();
    }


    public ScreenDrawer getScreenDrawer() {
        return screenDrawer;
    }

}
