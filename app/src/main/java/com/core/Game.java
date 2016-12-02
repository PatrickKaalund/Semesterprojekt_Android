package com.core;


import android.content.Context;
import android.graphics.PointF;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.gamelogic.Control;
import com.gamelogic.DataContainer;
import com.gamelogic.EnemySpawner;
import com.gamelogic.ItemSpawner;
import com.gamelogic.MapBorder;
import com.gamelogic.Player;
import com.gamelogic.PlayerRemote;
import com.graphics.BackgroundEntity;
import com.graphics.BackgroundFactory;
import com.graphics.FPSMeasuring;
import com.graphics.OurGLSurfaceView;
import com.network.Firebase.NetworkHandler;
import com.views.DropDownMenu;

import java.util.Random;

import io.github.controlwear.virtual.joystick.android.JoystickView;


public class Game implements Runnable {

    //=============== Threads =================
    private Thread thread;
    private FPSMeasuring fpsMeasuring;
    private int latestFPS = 0;

    //================= Misc ==================
    private Context context;
    private Control control;

    //============= Screen Stuff ==============
    private OurGLSurfaceView glSurfaceView;
    private static final int MAP_OUTER_BOARDER_SIZE = 4000;
    private static final int MAP_GLOBAL_START_POS = MAP_OUTER_BOARDER_SIZE / 2;
    private static final int SCREEN_CONTAINMENT_OFFSET = 150;
//    private  final int GLOBAL_START_POS_X;
//    private  final int GLOBAL_START_POS_Y;


    //============= Network stuff =============
    private NetworkHandler networkHandler;
    //~~~~~~~~ Remote game components ~~~~~~~~
    private PlayerRemote playerRemote;

    //============ Game components ============
    private Player player;
    private EnemySpawner enemySpawner;
    private ItemSpawner itemSpawner;
    private MapBorder mapBorder;
    private BackgroundEntity map;
    private BackgroundFactory mapFactory;

    //============== Game states ==============
    private static final int ENEMY_MAX_SPEED = 16;

    private int difficultyLevel = 1;
    private boolean isRunning;
    private boolean isPaused;
    private int enemySpawnInterval = 500;
    private int enemySpawmCounter = 0;
    private int enemySpeed = 3;
    private int enemyHealth = 20;
    private int timer = 0;
    private int second = 0;

    private boolean multiplayerGame;
    int shouldSpawnItem;
    int shouldSpawnEnemy;


    Random rand;

    enum GameStates_e {


    }


    public Game(Context context) {
        Log.d("Game", "Game created");
        rand = new Random();
        this.context = context;
        DataContainer.instance.gameContext = context;
        this.multiplayerGame = DataContainer.instance.multiplayerGame;
//        GLOBAL_START_POS_X = context.getResources().getDisplayMetrics().widthPixels / 2;
//        GLOBAL_START_POS_Y =  context.getResources().getDisplayMetrics().heightPixels / 2;

        glSurfaceView = new OurGLSurfaceView(context);
        networkHandler = new NetworkHandler(this.multiplayerGame);

        fpsMeasuring = new FPSMeasuring(context);
        fpsMeasuring.start();

        // Must be moved to background thread
        initGameComponents();
        gameStart();

        thread = new Thread(this);
        thread.start();

    }

    private void initGameComponents() {

//        Log.e("Game", "Waiting...");
//
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Log.e("Game", "Finished...");

        //Make a map
        mapFactory = new BackgroundFactory(
                R.drawable.backgrounddetailed3,
                context.getResources().getDisplayMetrics());
        map = mapFactory.createEntity(//Make background
                new PointF(MAP_OUTER_BOARDER_SIZE,
                        MAP_OUTER_BOARDER_SIZE),
                new PointF(MAP_GLOBAL_START_POS, MAP_GLOBAL_START_POS),
                SCREEN_CONTAINMENT_OFFSET);
//        mapBorder = new MapBorder(context);
        //Make game assets

        itemSpawner = new ItemSpawner(context);

        enemySpawner = new EnemySpawner(context);

        player = new Player(context, networkHandler, new PointF(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2));

        itemSpawner.setPlayer(player);

        if (this.multiplayerGame) {
            playerRemote = new PlayerRemote(networkHandler, map);
        }

        control = new Control(context, this);

        itemSpawner.spawnItemsRandom(3);

        enemySpawner.spawnEnemies(map, enemyHealth, enemySpeed, 3);

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
        glSurfaceView = null;
        thread = null;
    }

    public void updateGame() {

        player.move(control, map);
        player.update(control, enemySpawner);
        enemySpawner.update(player);
//        mapBorder.update();
        itemSpawner.update();

        if (this.multiplayerGame) {
            playerRemote.update();
        }

        if (timer++ >= 30) {
            timer = 0;
            second++;
        }

        if (second == 30) {
            difficultyLevel++;
        }

        if (second > 15 - difficultyLevel) {

            shouldSpawnEnemy = rand.nextInt(100 + difficultyLevel);

            if (shouldSpawnEnemy + difficultyLevel < (100 + difficultyLevel) / 2) {
                Log.w("GAME STATE", "Spawning enemy");
                enemySpawner.spawn(map, (int) (enemyHealth + difficultyLevel * 1.5), enemySpeed + difficultyLevel / 4);
            }

        }

        if (second > 30 - difficultyLevel) {

            shouldSpawnItem = rand.nextInt(100);
            shouldSpawnEnemy = rand.nextInt(100 + difficultyLevel);

            if (shouldSpawnItem < (100 + difficultyLevel) / 2) {
                Log.w("GAME STATE", "Spawning item");
                itemSpawner.spawnRandom();
            }
            second = 0;
        }

        if (player.currentState == Player.PlayerStates_e.GAME_OVER) {
            Log.e("GAME STATE", "################# GAME OVER ##################");
            isPaused = true;
        }


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


