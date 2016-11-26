package com.gamelogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.NetworkHandler;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {

    public enum weaponSelection_e {
        GUN,
        SHOTGUN,
        AK47,
        ALL_GUNS,
    }


    private Entity player;
    private NetworkHandler networkHandler;

    private ArrayList<Integer> joystickValues;
    private DisplayMetrics displayMetrics;
    private SpriteEntityFactory shootFactory = new SpriteEntityFactory(R.drawable.bullets, 100, 50, 3, 1, new PointF(400, 400));
    private Shooter gun;
    private int shotSpeedCounter = 0;
    private int shotSpeed = 10;
    private weaponSelection_e currentWeapon = weaponSelection_e.GUN;
    private DirectionLock directionLock;
    private Direction mapDirection;
    private SharedPreferences preferences;
    private final Context context;

    public Player(Context context, NetworkHandler networkHandler) {
    this.context = context;
        this.networkHandler = networkHandler;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        player = super.player;
        player.placeAt(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2);
        player.setPosition(new PointF(2000, 2000));
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

        displayMetrics = context.getResources().getDisplayMetrics();

        DataContainer.player = this;

        gun = new Shooter();
        shotSpeedCounter = shotSpeed;
        directionLock = new DirectionLock();
        super.direction.tag = 1;
        mapDirection = new Direction(super.direction);
        mapDirection.tag = 2;

    }

    public void update(Control control, EnemySpawner enemys) {
        if (shotSpeedCounter > shotSpeed && control.isShooting()) {
            gun.shoot(player.getPosition(), player.getRect(), super.direction, currentWeapon);
            shotSpeedCounter = 0;
        } else {
            shotSpeedCounter++;
        }
        gun.update(enemys);
    }

    int playerLockOld;
    int playerLock;

    public void move(Control control, BackgroundEntity map) {

        // read joystick
        joystickValues = control.getJoystickValues();

        int joystick_strength = (joystickValues.get(1));

        if (joystick_strength > 0) {

            int joystick_angle = joystickValues.get(0);
            super.direction.set(joystick_angle, joystick_strength);
            mapDirection.set(joystick_angle, joystick_strength);

            playerLock = directionLock.check(super.direction, map.getInnerBoarder(), new PointF(player.getRect().centerX(), player.getRect().centerY()), 0);
            player.move(super.direction);
            map.move(mapDirection, playerLock,directionLock.tblr);

//
//            switch (playerLock) {
//                case UNLOCKED:
//                    // Player is locked
//                    break;
//                case X_LOCKED:
//                    // Player is locked in Y (inverted)
//                    map.move(mapDirection,0,1);
//                    break;
//                case Y_LOCKED:
//                    // Player is locked in X (inverted)
//                    map.move(mapDirection,1,0);
//
//                    break;
//                case ALL_LOCKED:
//                    map.move(mapDirection,1,1);
//
//                    // Map is locked. Player is free
////                return;
//
//                    break;
//                default:
//                    Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
//                    break;
//            }


//
////            Log.d("Player", "Player pos: " + playerX + ", " + playerY);
////            Log.d("Player", "Player global pos: " + player.getPosition().x + ", " + player.getPosition().y);
//
//            int offset = 150;
//            int offset_top = 250;
////            Log.d("Player", "Player pos: " + player.getPosition().toString() + " + joystick angle: " + joystick_angle);
//            if (playerX >= offset && playerX <= displayMetrics.widthPixels - offset && playerY >= offset && playerY <= displayMetrics.heightPixels - offset_top) {
////                Log.d("Player", "Condition 1");
//                map.moveFrame(0, 0);
////                 full move
//                move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//            }
//            // move map Y
//            else if (playerX >= offset && playerX <= displayMetrics.widthPixels - offset) {
////                Log.d("Player", "Condition 2");
//
//                // unlock bottom Y? - movement 1
//                if (playerY <= displayMetrics.heightPixels - offset_top && (joystick_angle <= 180 && joystick_angle >= 0)) {
////                    Log.d("Player", "Condition 2.1");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // unlock top Y? - movement 1
//                else if (playerY >= offset && (joystick_angle >= 180 && joystick_angle <= 360)) {
////                    Log.d("Player", "Condition 2.2");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move X
//                else {
////                    Log.d("Player", "Condition 2.3");
//                    moveX(direction.velocity_X, joystick_angle);
//                    map.moveFrame(0, direction.velocity_Y);
//                    player.getPosition().x += direction.velocity_X;
//                }
//
//            }
//            // move map X
//            else if (playerY >= offset && playerY <= displayMetrics.heightPixels - offset_top) {
////                Log.d("Player", "In condition 3!");
//
//                // unlock left X? - movement 1
//                if (playerX <= displayMetrics.widthPixels - offset && (joystick_angle <= 90 || joystick_angle >= 270)) {
////                    Log.d("Player", "In condition 3.1!");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // unlock right X? - movement 1
//                else if (playerX >= offset && (joystick_angle >= 90 && joystick_angle <= 270)) {
////                    Log.d("Player", "In condition 3.2!");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move Y
//                else {
////                    Log.d("Player", "In condition 3.3!");
//                    moveY(direction.velocity_Y, joystick_angle);
//                    map.moveFrame(direction.velocity_X, 0);
//                    player.getPosition().y += direction.velocity_Y;
//                }
//            }
//            // check BL cornor
//            else if (playerY <= offset && playerX <= offset) {
////                Log.d("Player", "Condition 4");
//
//                if (joystick_angle <= 90 && joystick_angle >= 0) {
////                    Log.d("Player", "Condition 4.1");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move right
//                else if (joystick_angle >= 270 && joystick_angle <= 360) {
////                    Log.d("Player", "Condition 4.2");
//                    moveX(direction.velocity_X, joystick_angle);
//                    map.moveFrame(0, direction.velocity_Y);
//                }
//                // only move up
//                else if (joystick_angle >= 90 && joystick_angle <= 180) {
////                    Log.d("Player", "Condition 4.3");
//                    moveY(direction.velocity_Y, joystick_angle);
//                    map.moveFrame(direction.velocity_X, 0);
//
//                } else {
//                    move(0, 0, joystick_angle);
//                    map.moveFrame(direction.velocity_X, direction.velocity_Y);
//                }
//
//            }
//            // check TL cornor
//            else if (playerY >= displayMetrics.heightPixels - offset_top && playerX <= offset) {
////                Log.d("Player", "Condition 5");
//
//                if (joystick_angle <= 360 && joystick_angle >= 270) {
////                    Log.d("Player", "Condition 5.1");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move right
//                else if (joystick_angle >= 0 && joystick_angle <= 90) {
////                    Log.d("Player", "Condition 5.2");
//                    moveX(direction.velocity_X, joystick_angle);
//                    map.moveFrame(0, direction.velocity_Y);
//
//                }
//                // only move down
//                else if (joystick_angle >= 180 && joystick_angle <= 270) {
////                    Log.d("Player", "Condition 5.3");
//                    moveY(direction.velocity_Y, joystick_angle);
//                    map.moveFrame(direction.velocity_X, 0);
//
//                } else {
//                    move(0, 0, joystick_angle);
//                    map.moveFrame(direction.velocity_X, direction.velocity_Y);
//                }
//
//            }
//            // check TR cornor
//            else if (playerY >= displayMetrics.heightPixels - offset_top && playerX >= displayMetrics.widthPixels - offset) {
////                Log.d("Player", "Condition 6");
////                Log.d("Player", " angle " + joystick_angle);
//
//                if (joystick_angle < 270 && joystick_angle > 180) {
////                    Log.d("Player", "Condition 6.1");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move left
//                else if (joystick_angle > 90 && joystick_angle <= 180) {
////                    Log.d("Player", "Condition 6.2");
//                    moveX(direction.velocity_X, joystick_angle);
//                    map.moveFrame(0, direction.velocity_Y);
//                }
//                // only move down
//                else if (joystick_angle >= 270 && joystick_angle < 360) {
////                    Log.d("Player", "Condition 6.3");
//                    moveY(direction.velocity_Y, joystick_angle);
//                    map.moveFrame(direction.velocity_X, 0);
//                } else {
//                    move(0, 0, joystick_angle);
//                    map.moveFrame(direction.velocity_X, direction.velocity_Y);
//                }
//            }
//            // check BR cornor
//            else if (playerY <= offset && playerX >= offset) {
////                Log.d("Player", "Condition 7: " + joystick_angle);
////
//                if (joystick_angle <= 180 && joystick_angle >= 90) {
////                    Log.d("Player", "Condition 7.1");
//                    // full move
//                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
//                }
//                // only move left
//                else if (joystick_angle >= 180 && joystick_angle <= 270) {
////                    Log.d("Player", "Condition 7.2");
//                    moveX(direction.velocity_X, joystick_angle);
//                    map.moveFrame(0, direction.velocity_Y);
//
//                }
//                // only move up
//                else if (joystick_angle >= 0 && joystick_angle <= 90) {
////                    Log.d("Player", "Condition 7.3");
//                    moveY(direction.velocity_Y, joystick_angle);
//                    map.moveFrame(direction.velocity_X, 0);
//                } else {
//                    move(0, 0, joystick_angle);
//                    map.moveFrame(direction.velocity_X, direction.velocity_Y);
//                }
//            }
            player.drawNextSprite();
            // networkHandler.updatePlayerPosition(playerStill.getRect().centerX(), playerStill.getRect().centerY());
        } else {

            switch (currentWeapon) {

                case GUN:
                    player.setCurrentSprite(45);

                    break;
                case SHOTGUN:
                    player.setCurrentSprite(23);

                    break;
                case AK47:
                    player.setCurrentSprite(0);

                    break;
                default:
                    Log.e("PLAYER", "DEFULTED IN PLAYE::move: switch (currentWeapon)");
                    break;
            }
            // map.moveFrame(0, 0);
        }

//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    private void move(float deltaX, float deltaY, int angle) {
        player.getPosition().x += deltaX;
        player.getPosition().y += deltaY;
        player.moveBy(deltaX, deltaY, angle);
    }

    private void moveX(float deltaX, int angle) {
        player.moveBy(deltaX, 0, angle);
//        player.getPosition().x += deltaX;
    }

    private void moveY(float deltaY, int angle) {
        player.moveBy(0, deltaY, angle);
//        player.getPosition().y += deltaY;
    }

    public RectF getRect() {
        return this.player.getRect();
    }

    public PointF getPos() {
        return this.player.getPosition();
    }

    public Entity getPlayer() {
        return player;
    }

    @Override
    public void doDamge(int damge) {
        super.health -= damge;
        if (super.health <= 0) {
            Log.e("PLAYER IS DEAD", "+++++++++++++++++++++++++  PLAYER IS DEAD ++++++++++++++++++++");
        }

    }

    public Entity getEntity() {
        return player;
    }

    public weaponSelection_e getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(weaponSelection_e currentWeapon) {

        if (preferences.getBoolean("sound", true)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.reload);
            mediaPlayer.start();
        }

        int currentSprite = player.getCurrentSprite();
        switch (currentWeapon) {

            case GUN:
                player.setAnimationOrder(new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64});
                
                switch (currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite + 23);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite + 46);
                        break;
                }
                break;

            case SHOTGUN:
                player.setAnimationOrder(new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42});

                switch (currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite - 23);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite + 23);
                        break;
                }
                break;

            case AK47:
                player.setAnimationOrder(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

                switch (currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite - 46);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite - 23);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite);
                        break;
                }
                break;
        }
        this.currentWeapon = currentWeapon;
    }
}
