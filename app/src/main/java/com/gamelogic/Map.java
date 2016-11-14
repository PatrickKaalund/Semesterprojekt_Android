package com.gamelogic;


import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.core.GUpdateable;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.BackgroundFactory;
import com.graphics.EnemySpawner;
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.SpriteEntityFactory;
import com.graphics.Direction;

import java.util.ArrayList;

import static com.graphics.Direction.UNLOCK;
import static com.graphics.Direction.X;
import static com.graphics.Direction.Y;
import static com.graphics.GraphicsTools.LL;

public class Map extends GUpdateable {

    private DisplayMetrics metrics;
    BackgroundEntity mapBackground;
    BackgroundFactory mapFactory;
    Direction velMap;
    RectF boarder = new RectF(0f, 0f, 2000f, 2000f);
    RectF boarderInder;
    RectF outerBorader;
    Direction mapDirektion;

    private EnemySpawner enemySpawner;


    public Map(Context c) {
        Log.d("Map", "making map");
        game.objectsToUpdate.add(this);
        this.metrics = c.getResources().getDisplayMetrics();
        mapFactory = new BackgroundFactory(R.drawable.backgrounddetailed2, metrics);
        mapBackground = mapFactory.crateEntity();
        float ratio = metrics.widthPixels / metrics.heightPixels;
//        velMap = new Direction();
        mapDirektion = new Direction();
        boarderInder = new RectF(200f, 200f, metrics.widthPixels - 200, metrics.heightPixels - 200);
        outerBorader = new RectF(0f, 0f, 2000f, 2000f);
        Log.d("Map", "boarderInder: " + GraphicsTools.rectToString(boarderInder));

//        enemySpawner = new EnemySpawner(c);
//        enemySpawner.spawnEnemies(100, 10, 10);

    }


    int rotation = 0;

    //    @Override
//    public void update() {
//        // read joystick
//        ArrayList<Integer> joystickValues = game.getControl().getJoystickValues();
//
//        int joystick_angle = -joystickValues.get(0);
//        float joystick_strength = ((float) joystickValues.get(1) / 10000);
//
////        Log.d("Player", "Angle: " + joystick_angle);
////        Log.d("Player", "Strength: " + joystick_strength);
//
//        vel.angle = joystick_angle;
//        vel.velocety = joystick_strength;
//
//        mapBackground.moveFrame(vel);
//    }
    @Override
    public void update() {
//        // read joystick
//        ArrayList<Integer> joystickValues = game.getControl().getJoystickValues();
//
//        int joystick_angle = joystickValues.get(0);
//        float joystick_strength = ((float) joystickValues.get(1) / 50);
//
//        velMap.set(joystick_angle, joystick_strength / metrics.heightPixels);
//
////        mapBackground.moveFrame(velMap);
//        RectF pboarder = game.getPlayer().getRect();

//        if (joystick_strength > 0) {
//
//            if (boarderInder.contains(pboarder.centerX(), pboarder.centerY())) {
////                Log.e("Map", "############ lock ##############");
//
//                mapBackground.setLock(LockDirection.ALL);
////                player.setLock(false);
//
//            } else {
////                Log.e("Map", "############ unlock ##############");
//                mapBackground.setLock(LockDirection.ALL);
////                player.setLock(true);
//            }
//
//        }
//        enemySpawner.update(velMap);
    }


    public void move(Entity player, Direction direction) {

        if (direction.getVelocity() != 0) {

            direction.lockInside(boarderInder, player.getRect().centerX(), player.getRect().centerY());
            mapDirektion.tranfareWithRatio(player.move(direction));
            mapDirektion.lockInside(outerBorader, player.getPosition().x, player.getPosition().y);

            switch (mapDirektion.lock) {
                case X:
                    player.getPosition().y += direction.calcVelocity_Y();
                    break;
                case Y:
                    player.getPosition().x += direction.calcVelocity_X();
                    break;
                case UNLOCK:
                    player.getPosition().x += direction.calcVelocity_X();
                    player.getPosition().y += direction.calcVelocity_Y();
                    break;
            }
            Direction mapd = mapBackground.moveFrame(mapDirektion);
//            LL(this, "Map x: "+x+" y: "+y);
        }


    }

}

























