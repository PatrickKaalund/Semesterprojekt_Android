package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.graphics.Entity;
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

    public Player(Context context, NetworkHandler networkHandler) {
        game.objectsToUpdate.add(this);

        this.networkHandler = networkHandler;

        player = super.player;
        player.placeAt(400, 400);
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

        displayMetrics = context.getResources().getDisplayMetrics();

        DataContainer.player = this;
    }

    @Override
    public void update() {

        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1));

        super.direction.set(joystick_angle, joystick_strength);

        if(joystick_strength > 0){
            int offset = 150;
            int offset_top = 250;
//            Log.d("Player", "Player pos: " + player.getPosition().toString() + " + joystick angle: " + joystick_angle);
            if(player.getPosition().x >= offset && player.getPosition().x <= displayMetrics.widthPixels - offset && player.getPosition().y >= offset && player.getPosition().y <= displayMetrics.heightPixels - offset_top){
//                Log.d("Player", "Condition 1");
                game.map.move(0, 0);
//                 full move
                move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
            }
            else if(player.getPosition().x >= offset && player.getPosition().x <= displayMetrics.widthPixels - offset){
//                Log.d("Player", "Condition 2");
                game.map.move(0, direction.velocity_Y);
                // unlock bottom Y? - movement 1
                if (player.getPosition().y <= offset && (joystick_angle <= 180 && joystick_angle >= 0)) {
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // unlock top Y? - movement 1
                else if (player.getPosition().y >= displayMetrics.heightPixels - offset_top && (joystick_angle >= 180 && joystick_angle <= 360)) {
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move X
                else {
                    moveX(direction.calcVelocity_X(), joystick_angle);
                }
            }
            else if(player.getPosition().y >= offset && player.getPosition().y <= displayMetrics.heightPixels - offset_top) {
//                Log.d("Player", "In condition 3!");
                game.map.move(direction.velocity_X, 0);
                // unlock left X? - movement 1
                if (player.getPosition().x <= offset && (joystick_angle <= 90 || joystick_angle >= 270)) {
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // unlock right X? - movement 1
                else if (player.getPosition().x >= displayMetrics.widthPixels - offset && (joystick_angle >= 90 && joystick_angle <= 270)) {
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move Y
                else {
                    moveY(direction.calcVelocity_Y(), joystick_angle);
                }
            }
            // check BL cornor
            else if(player.getPosition().y <= offset && player.getPosition().x <= offset){
//                Log.d("Player", "Condition 4");
                game.map.move(direction.velocity_X, direction.velocity_Y);

                if(joystick_angle <= 90 && joystick_angle >= 0){
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move right
                else if(joystick_angle >= 270 && joystick_angle <= 360){
                    moveX(direction.calcVelocity_X(), joystick_angle);
                }
                // only move up
                else if(joystick_angle >= 90 && joystick_angle <= 180){
                    moveY(direction.calcVelocity_Y(), joystick_angle);
                }
            }
            // check TL cornor
            else if(player.getPosition().y >= displayMetrics.heightPixels - offset_top && player.getPosition().x <= offset){
//                Log.d("Player", "Condition 5");
                game.map.move(direction.velocity_X, direction.velocity_Y);

                if(joystick_angle <= 360 && joystick_angle >= 270){
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move right
                else if(joystick_angle >= 0 && joystick_angle <= 90){
                    moveX(direction.calcVelocity_X(), joystick_angle);
                }
                // only move down
                else if(joystick_angle >= 180 && joystick_angle <= 270){
                    moveY(direction.calcVelocity_Y(), joystick_angle);
                }
            }
            // check TR cornor
            else if(player.getPosition().y >= displayMetrics.heightPixels - offset_top && player.getPosition().x >= offset){
//                Log.d("Player", "Condition 6");
                game.map.move(direction.velocity_X, direction.velocity_Y);

                if(joystick_angle <= 270 && joystick_angle >= 180){
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move left
                else if(joystick_angle >= 90 && joystick_angle <= 180){
                    moveX(direction.calcVelocity_X(), joystick_angle);
                }
                // only move down
                else if(joystick_angle >= 270 && joystick_angle <= 360){
                    moveY(direction.calcVelocity_Y(), joystick_angle);
                }
            }
            // check BR cornor
            else if(player.getPosition().y <= offset && player.getPosition().x >= offset){
//                Log.d("Player", "Condition 7: " + joystick_angle);
                game.map.move(direction.velocity_X, direction.velocity_Y);

                if(joystick_angle <= 180 && joystick_angle >= 90){
                    // full move
                    move(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move left
                else if(joystick_angle >= 180 && joystick_angle <= 270){
                    moveX(direction.calcVelocity_X(), joystick_angle);
                }
                // only move up
                else if(joystick_angle >= 0 && joystick_angle <= 90){
                    moveY(direction.calcVelocity_Y(), joystick_angle);
                }
            }
            else{
                Log.d("Player", "NOT GOOD !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
            player.drawNextSprite();
           // networkHandler.updatePlayerPosition(playerStill.getRect().centerX(), playerStill.getRect().centerY());
        }
        else{
            player.setCurrentSprite(0);
            game.map.move(0, 0);
        }

//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    private void move(float deltaX, float deltaY, int angle){
        player.getPosition().x += super.direction.velocity_X;
        player.getPosition().y += super.direction.velocity_Y;
        player.moveBy(deltaX, deltaY, angle);
    }

    private void moveX(float deltaX, int angle){
        player.moveBy(deltaX, 0, angle);
        player.getPosition().x += super.direction.velocity_X;
    }

    private void moveY(float deltaY, int angle){
        player.moveBy(0, deltaY, angle);
        player.getPosition().y += super.direction.velocity_Y;
    }

    public RectF getRect(){ return this.player.getRect(); }
    public PointF getPos(){ return this.player.getPosition(); }
}
