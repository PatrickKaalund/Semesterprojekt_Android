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

public class Enemy extends Creature {


    private Entity enemy;
    private ArrayList<Integer> joystickValues;

    Direction direction;

    public Enemy(SpriteEntityFactory enemyFactory, int health, int speed, PointF startLocation) {

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

        // calculate angle between player and enemy
        int angle = (int) Math.toDegrees(Math.atan2(400.0F - enemy.getPosition().y, 400.0F - enemy.getPosition().x));
        if (angle < 0) {
            angle += 360;
        }

        direction.set(angle, this.speed);

        enemy.move(direction);

        enemy.drawNextSprite();
    }

    public RectF getRect(){ return this.enemy.getRect(); }
}