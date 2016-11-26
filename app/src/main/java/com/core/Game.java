package com.core;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.gamelogic.Control;
import com.gamelogic.EnemySpawner;
import com.gamelogic.ItemSpawner;
import com.gamelogic.MapBorder;
import com.gamelogic.Player;
import com.graphics.BackgroundEntity;
import com.graphics.BackgroundFactory;
import com.graphics.FPSMeasuring;
import com.graphics.OurGLSurfaceView;
import com.network.Firebase.NetworkHandler;
import com.views.DropDownMenu;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static com.gamelogic.DataContainer.gameContext;

public class Game implements Runnable {

    //=============== Threads =================
    private Thread thread;
    private FPSMeasuring fpsMeasuring;
    private int latestFPS = 0;

    //================= Misc ==================
    private Context context;
    private OurGLSurfaceView glSurfaceView;
    private Control control;

    //============= Network stuff =============
    private NetworkHandler networkHandler;
    //~~~~~~~~ Remote game components ~~~~~~~~
    //private PlayerRemote playerRemote;

    //============ Game components ============
    private Player player;
    private EnemySpawner enemySpawner;
    private ItemSpawner itemSpawner;
    private MapBorder mapBorder;
    private BackgroundEntity map;
    private BackgroundFactory mapFactory;

    //============== Game states ==============
    private static final int ENEMY_BASE_HELTH = 100;
    private static final int ENEMY_BASE_SPEED = 5;
    private static final int DIFFICULTY_MULTIPLIER = 10;

    private int currentDifficulty = 0;
    private boolean isRunning;
    private boolean isPaused;
    private int enemySpawnInterval = 500;
    private int enemySpawmCounter = 0;

    enum GameStates_e {

    }


    public Game(Context context) {
        Log.d("Game", "Game created");
        this.context = context;
        gameContext = context;
        glSurfaceView = new OurGLSurfaceView(context);
        networkHandler = new NetworkHandler();
        fpsMeasuring = new FPSMeasuring(context);


        initGameComponents();
        fpsMeasuring.start();
        gameStart();

        thread = new Thread(this);
        thread.start();

    }

    private void initGameComponents() {
        player = new Player(context, networkHandler);
        control = new Control(context, this);
        mapFactory = new BackgroundFactory(R.drawable.backgrounddetailed_resized_grid, context.getResources().getDisplayMetrics());
        map = mapFactory.createEntity(4000, 4000); //Make background
        itemSpawner = new ItemSpawner(context);
        enemySpawner = new EnemySpawner(context);
        mapBorder = new MapBorder(context);
//        enemySpawner.spawnEnemies(ENEMY_BASE_HELTH, ENEMY_BASE_SPEED, 1);
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
                updateGame();
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
//                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void gameStart() {
        isRunning = true;
        isPaused = false;
        fpsMeasuring.startFPS();
        updateGame();
    }

    public void gamePause() {
        isPaused = true;
        fpsMeasuring.stopFPS();
    }

    public void gameStop() {
        isRunning = false;
        fpsMeasuring.stopFPS();
    }

    public void updateGame() {

        player.move(control, map);
        player.update(control, enemySpawner);
        enemySpawner.update(player);
        mapBorder.update();
        itemSpawner.update();
//
//        if (enemySpawmCounter++ >= enemySpawnInterval) {
//            enemySpawmCounter = 0;
//            enemySpawnInterval -= currentDifficulty;
//            enemySpawner.spawn(100 + currentDifficulty, ENEMY_BASE_SPEED + (currentDifficulty / 3));
//            currentDifficulty += DIFFICULTY_MULTIPLIER;
//            Log.w("Game", "!!! Spawning enemy !!! watch out :-)");
//        }

    }

    // Getters
    public OurGLSurfaceView getGameView() {
        return glSurfaceView;
    }

    //    public Control getControl() {
//        return this.control;
//    }
    public int getFPS() {
        return fpsMeasuring.latestFPS;
    }

    public Player getPlayer() {
        return this.player;
    }

    public BackgroundEntity getMap() {
        return map;
    }
}


