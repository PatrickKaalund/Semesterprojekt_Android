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
    private DataContainer dataContainer;

    private ArrayList<Integer> joystickValues;
    private DisplayMetrics displayMetrics;

    public Player(Context context, NetworkHandler networkHandler) {
        game.objectsToUpdate.add(this);

        this.networkHandler = networkHandler;

        player = super.player;
        player.placeAt(400, 400);
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

        dataContainer = new DataContainer();
        dataContainer.setPlayer(this);

        displayMetrics = context.getResources().getDisplayMetrics();

    }

    @Override
    public void update() {

        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1));

        super.direction.set(joystick_angle, joystick_strength);

//        game.map.move(player, super.direction);

        if(joystick_strength > 0){
            int offset = 150;
//            Log.d("Player", "Player pos: " + player.getPosition().toString() + " + joystick angle: " + joystick_angle);
            if(player.getPosition().x > offset && player.getPosition().x < displayMetrics.widthPixels - offset && player.getPosition().y > offset && player.getPosition().y < displayMetrics.heightPixels - offset){
                player.getPosition().x += super.direction.velocity_X;
                player.getPosition().y += super.direction.velocity_Y;
                player.moveBy(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
            }
            else if(player.getPosition().x > offset && player.getPosition().x < displayMetrics.widthPixels - offset){
                // unlock bottom Y? - movement 1
                if (player.getPosition().y < offset && (joystick_angle < 180 && joystick_angle > 0)) {
                    player.getPosition().x += super.direction.velocity_X;
                    player.getPosition().y += super.direction.velocity_Y;
                    player.moveBy(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // unlock top Y? - movement 1
                else if (player.getPosition().y > displayMetrics.heightPixels - offset && (joystick_angle > 180 && joystick_angle < 360)) {
                    player.getPosition().x += super.direction.velocity_X;
                    player.getPosition().y += super.direction.velocity_Y;
                    player.moveBy(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move X
                else {
                    player.moveBy(direction.calcVelocity_X(), 0, joystick_angle);
                    player.getPosition().x += super.direction.velocity_X;
                }
            }
            else if(player.getPosition().y > offset && player.getPosition().y < displayMetrics.heightPixels - offset) {
                // unlock left X? - movement 1
                if (player.getPosition().x < offset && (joystick_angle < 90 || joystick_angle > 270)) {
                    player.getPosition().x += super.direction.velocity_X;
                    player.getPosition().y += super.direction.velocity_Y;
                    player.moveBy(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // unlock right X? - movement 1
                else if (player.getPosition().x > displayMetrics.widthPixels - offset && (joystick_angle > 90 && joystick_angle < 270)) {
                    player.getPosition().x += super.direction.velocity_X;
                    player.getPosition().y += super.direction.velocity_Y;
                    player.moveBy(direction.calcVelocity_X(), direction.calcVelocity_Y(), joystick_angle);
                }
                // only move Y
                else {
                    player.moveBy(0, direction.calcVelocity_Y(), joystick_angle);
                    player.getPosition().y += super.direction.velocity_Y;
                }
            }
            player.drawNextSprite();
           // networkHandler.updatePlayerPosition(playerStill.getRect().centerX(), playerStill.getRect().centerY());
        }else{
            player.setCurrentSprite(0);
        }



//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    public RectF getRect(){ return this.player.getRect(); }
    public PointF getPos(){ return this.player.getPosition(); }
}
