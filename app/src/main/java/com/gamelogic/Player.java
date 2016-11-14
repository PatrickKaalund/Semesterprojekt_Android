package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends Creature {

    private SpriteEntityFactory playerFactory;
    private Entity player;

    private ArrayList<Integer> joystickValues;

    Direction direction;

    public Player() {
        game.objectsToUpdate.add(this);

        super.speed = 5;
        super.health = 100;

        playerFactory = new SpriteEntityFactory(R.drawable.soldier_topdown_adjusted, 200, 200, 4, 2, new PointF(400, 400));

        player = playerFactory.createEntity();
        player.setCurrentSprite(0);
        player.setAngleOffSet(90);
        player.setAnimationDivider(5);
        player.setAnimationOrder(new int[]{0, 1, 2, 3, 4});

        direction = new Direction(speed,200);

        joystickValues = new ArrayList<>();
    }

    @Override
    public void update() {

        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1));

        direction.set(joystick_angle, joystick_strength);

        game.map.move(player, direction);

        if (joystick_strength > 0) {
            player.drawNextSprite();
        } else {
            player.setCurrentSprite(0);
        }

//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    public RectF getRect() {
        return this.player.getRect();
    }
//    public void setLock(LockDirection lockDirection){ player.setLock(lockDirection);}
}
