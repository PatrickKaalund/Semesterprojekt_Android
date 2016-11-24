package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.NetworkHandler;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {


    private Entity player;
    private NetworkHandler networkHandler;

    private ArrayList<Integer> joystickValues;
    private DisplayMetrics displayMetrics;
    private SpriteEntityFactory shootFactory = new SpriteEntityFactory(R.drawable.beam_green, 100, 50, 3, 1, new PointF(400, 400));
    private Shooter gun;
    private int shotSpeedCounter = 0;
    private int shotSpeed = 10;
    private int currentWeapon = 0;

    public Player(Context context, NetworkHandler networkHandler) {
        game.objectsToUpdate.add(this);

        this.networkHandler = networkHandler;

        player = super.player;
        player.placeAt(400, 400);
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

        displayMetrics = context.getResources().getDisplayMetrics();

        DataContainer.player = this;

        gun = new Shooter();
        shotSpeedCounter = shotSpeed;

    }

    @Override
    public void update() {

        if (shotSpeedCounter > shotSpeed && game.getControl().isShooting()) {
            gun.shoot(player.getPosition(), player.getRect(), super.direction);
            shotSpeedCounter = 0;
        }else{
            shotSpeedCounter++;
        }

        gun.update();
        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1));

        super.direction.set(joystick_angle, joystick_strength);

        if (joystick_strength > 0) {
            float playerX = player.getRect().centerX();
            float playerY = player.getRect().centerY();

//            Log.d("Player", "Player pos: " + playerX + ", " + playerY);
//            Log.d("Player", "Player global pos: " + player.getPosition().x + ", " + player.getPosition().y);

            int offset = 150;
            int offset_top = 250;
//            Log.d("Player", "Player pos: " + player.getPosition().toString() + " + joystick angle: " + joystick_angle);
            if (playerX >= offset && playerX <= displayMetrics.widthPixels - offset && playerY >= offset && playerY <= displayMetrics.heightPixels - offset_top) {
//                Log.d("Player", "Condition 1");
                game.map.move(0, 0);
//                 full move
                move(direction.velocity_X, direction.velocity_Y, joystick_angle);
            }
            // move map Y
            else if (playerX >= offset && playerX <= displayMetrics.widthPixels - offset) {
//                Log.d("Player", "Condition 2");

                // unlock bottom Y? - movement 1
                if (playerY <= displayMetrics.heightPixels - offset_top && (joystick_angle <= 180 && joystick_angle >= 0)) {
//                    Log.d("Player", "Condition 2.1");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // unlock top Y? - movement 1
                else if (playerY >= offset && (joystick_angle >= 180 && joystick_angle <= 360)) {
//                    Log.d("Player", "Condition 2.2");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move X
                else {
//                    Log.d("Player", "Condition 2.3");
                    moveX(direction.velocity_X, joystick_angle);
                    game.map.move(0, direction.velocity_Y);
                    player.getPosition().x += direction.velocity_X;
                }

            }
            // move map X
            else if (playerY >= offset && playerY <= displayMetrics.heightPixels - offset_top) {
//                Log.d("Player", "In condition 3!");

                // unlock left X? - movement 1
                if (playerX <= displayMetrics.widthPixels - offset && (joystick_angle <= 90 || joystick_angle >= 270)) {
//                    Log.d("Player", "In condition 3.1!");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // unlock right X? - movement 1
                else if (playerX >= offset && (joystick_angle >= 90 && joystick_angle <= 270)) {
//                    Log.d("Player", "In condition 3.2!");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move Y
                else {
//                    Log.d("Player", "In condition 3.3!");
                    moveY(direction.velocity_Y, joystick_angle);
                    game.map.move(direction.velocity_X, 0);
                    player.getPosition().y += direction.velocity_Y;
                }
            }
            // check BL cornor
            else if (playerY <= offset && playerX <= offset) {
//                Log.d("Player", "Condition 4");

                if (joystick_angle <= 90 && joystick_angle >= 0) {
//                    Log.d("Player", "Condition 4.1");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move right
                else if (joystick_angle >= 270 && joystick_angle <= 360) {
//                    Log.d("Player", "Condition 4.2");
                    moveX(direction.velocity_X, joystick_angle);
                    game.map.move(0, direction.velocity_Y);
                }
                // only move up
                else if (joystick_angle >= 90 && joystick_angle <= 180) {
//                    Log.d("Player", "Condition 4.3");
                    moveY(direction.velocity_Y, joystick_angle);
                    game.map.move(direction.velocity_X, 0);

                } else {
                    move(0, 0, joystick_angle);
                    game.map.move(direction.velocity_X, direction.velocity_Y);
                }

            }
            // check TL cornor
            else if (playerY >= displayMetrics.heightPixels - offset_top && playerX <= offset) {
//                Log.d("Player", "Condition 5");

                if (joystick_angle <= 360 && joystick_angle >= 270) {
//                    Log.d("Player", "Condition 5.1");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move right
                else if (joystick_angle >= 0 && joystick_angle <= 90) {
//                    Log.d("Player", "Condition 5.2");
                    moveX(direction.velocity_X, joystick_angle);
                    game.map.move(0, direction.velocity_Y);

                }
                // only move down
                else if (joystick_angle >= 180 && joystick_angle <= 270) {
//                    Log.d("Player", "Condition 5.3");
                    moveY(direction.velocity_Y, joystick_angle);
                    game.map.move(direction.velocity_X, 0);

                } else {
                    move(0, 0, joystick_angle);
                    game.map.move(direction.velocity_X, direction.velocity_Y);
                }

            }
            // check TR cornor
            else if (playerY >= displayMetrics.heightPixels - offset_top && playerX >= displayMetrics.widthPixels - offset) {
//                Log.d("Player", "Condition 6");
//                Log.d("Player", " angle " + joystick_angle);

                if (joystick_angle < 270 && joystick_angle > 180) {
//                    Log.d("Player", "Condition 6.1");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move left
                else if (joystick_angle > 90 && joystick_angle <= 180) {
//                    Log.d("Player", "Condition 6.2");
                    moveX(direction.velocity_X, joystick_angle);
                    game.map.move(0, direction.velocity_Y);
                }
                // only move down
                else if (joystick_angle >= 270 && joystick_angle < 360) {
//                    Log.d("Player", "Condition 6.3");
                    moveY(direction.velocity_Y, joystick_angle);
                    game.map.move(direction.velocity_X, 0);
                } else {
                    move(0, 0, joystick_angle);
                    game.map.move(direction.velocity_X, direction.velocity_Y);
                }
            }
            // check BR cornor
            else if (playerY <= offset && playerX >= offset) {
//                Log.d("Player", "Condition 7: " + joystick_angle);
//
                if (joystick_angle <= 180 && joystick_angle >= 90) {
//                    Log.d("Player", "Condition 7.1");
                    // full move
                    move(direction.velocity_X, direction.velocity_Y, joystick_angle);
                }
                // only move left
                else if (joystick_angle >= 180 && joystick_angle <= 270) {
//                    Log.d("Player", "Condition 7.2");
                    moveX(direction.velocity_X, joystick_angle);
                    game.map.move(0, direction.velocity_Y);

                }
                // only move up
                else if (joystick_angle >= 0 && joystick_angle <= 90) {
//                    Log.d("Player", "Condition 7.3");
                    moveY(direction.velocity_Y, joystick_angle);
                    game.map.move(direction.velocity_X, 0);
                } else {
                    move(0, 0, joystick_angle);
                    game.map.move(direction.velocity_X, direction.velocity_Y);
                }
            }
            player.drawNextSprite();
            // networkHandler.updatePlayerPosition(playerStill.getRect().centerX(), playerStill.getRect().centerY());
        } else {
            if (currentWeapon == 0) {
                player.setCurrentSprite(45);
            } else if (currentWeapon == 1) {
                player.setCurrentSprite(23);
            } else if (currentWeapon == 2) {
                player.setCurrentSprite(0);
            }
            game.map.move(0, 0);
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

    public Entity getEntity() {
        return player;
    }

    public int getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(int currentWeapon) {
        this.currentWeapon = currentWeapon;
    }
}
