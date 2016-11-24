package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Enemy extends Creature {


    private Entity enemy;
    private ArrayList<Integer> joystickValues;

    Direction direction;

    public Enemy(SpriteEntityFactory enemyFactory, int health, int speed, PointF startLocation, DataContainer dataContainer) {

        super.speed = speed;
        super.health = health;

        enemy = enemyFactory.createEntity();
        enemy.placeAt(startLocation.x, startLocation.y);
        enemy.setCurrentSprite(4);
        enemy.setAngleOffSet(-135);     // pointing right
        enemy.setAnimationDivider(3);
        enemy.setAnimationOrder(new int[]{4, 5, 6, 7, 8, 9, 10, 11});

        direction = new Direction();

        joystickValues = new ArrayList<>();

    }

    @Override
    public void update() {

        PointF playerPos = DataContainer.player.getPos();
//        Log.d("Enemy", "PlayerPos: " + playerPos.toString() + " enemyPos: " + enemy.getPosition().toString());

        // calculate angle between player and enemy
        int angle = (int) Math.toDegrees(Math.atan2(playerPos.y - enemy.getPosition().y, playerPos.x - enemy.getPosition().x));
//        if (angle < 0) {
//            angle += 360;
//        }

        direction.set(angle, this.speed);

        // Adjust for map movement
        enemy.moveBy(DataContainer.mapMovement.x, DataContainer.mapMovement.y, 0);

        enemy.moveBy(direction.velocity_X, direction.velocity_Y, angle);

//        enemy.move(direction);
        enemy.getPosition().x += direction.velocity_X;
        enemy.getPosition().y += direction.velocity_Y;

        enemy.drawNextSprite();
    }

    public RectF getRect(){ return this.enemy.getRect(); }
}