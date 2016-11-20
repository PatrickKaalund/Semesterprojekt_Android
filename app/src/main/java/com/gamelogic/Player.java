package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.provider.ContactsContract;
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
    private DataContainer dataContainer;

    private ArrayList<Integer> joystickValues;

    public Player(NetworkHandler networkHandler) {
        game.objectsToUpdate.add(this);

        this.networkHandler = networkHandler;

        player = super.player;
        player.placeAt(400, 400);
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

        dataContainer = new DataContainer();
        dataContainer.setPlayer(this);
    }

    @Override
    public void update() {

        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1));

        super.direction.set(joystick_angle, joystick_strength);

        game.map.move(player, super.direction);

        if(joystick_strength > 0){
//            player.getPosition().x += super.direction.velocity_X;
//            player.getPosition().y += super.direction.velocity_Y;
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
