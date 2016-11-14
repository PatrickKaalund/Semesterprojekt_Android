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
        enemy.setAngleOffSet(-45);
        enemy.setAnimationDivider(3);
        enemy.setAnimationOrder(new int[]{4, 5, 6, 7, 8, 9, 10, 11});

//        direction = new Direction();

        joystickValues = new ArrayList<>();
    }

    public void update(Direction velMap) {

        int angle = 0;

        // Adjust for map vel
//        direction.set(angle, velMap.getVelocity());
        direction.set(angle, 0);

        enemy.move(direction);

        enemy.drawNextSprite();
    }

    public RectF getRect(){ return this.enemy.getRect(); }
//    public void setLock(LockDirection lockDirection){ enemy.setLock(lockDirection);}

    @Override
    public void update() {
            // SHOULD BE REMOVED! INTERFACE ISSUES ATM
    }
}