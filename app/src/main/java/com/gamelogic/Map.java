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
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.Direction;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.NetworkHandler;

import static com.graphics.Direction.ALL;
import static com.graphics.Direction.UNLOCK;
import static com.graphics.Direction.X;
import static com.graphics.Direction.Y;

public class Map extends GUpdateable {

    private DisplayMetrics metrics;
    BackgroundEntity mapBackground;
    BackgroundFactory mapFactory;
    Direction velMap;
    RectF boarderInder;
    RectF outerBorader;
    Direction mapDirektion;
    private MapBorder mapBorder;

    Entity borderEntityLeft;

    private SpriteEntityFactory borderFactory;

//    private PlayerRemote playerRemote;
    private EnemySpawner enemySpawner;
    private ItemSpawner itemSpawner;

    public Map(Context c, NetworkHandler networkHandler) {
        Log.d("Map", "making map");
        game.objectsToUpdate.add(this);
        this.metrics = c.getResources().getDisplayMetrics();
        mapFactory = new BackgroundFactory(R.drawable.backgrounddetailed2, metrics);
        mapBackground = mapFactory.createEntity(4000, 4000);
        float ratio = metrics.widthPixels / metrics.heightPixels;
//        velMap = new Direction();
        mapDirektion = new Direction();
        boarderInder = new RectF(200f, 200f, metrics.widthPixels - 200, metrics.heightPixels - 200);
        outerBorader = new RectF(0f, 0f, 2000f, 2000f);
        Log.d("Map", "boarderInder: " + GraphicsTools.rectToString(boarderInder));

        itemSpawner = new ItemSpawner(c);
        itemSpawner.spawnItemsRandom(10);

        enemySpawner = new EnemySpawner(c);
        enemySpawner.spawnEnemies(100, 5, 10);

        mapBorder = new MapBorder(c);

//        playerRemote = new PlayerRemote(networkHandler);

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
////                playerStill.setLock(false);
//
//            } else {
////                Log.e("Map", "############ unlock ##############");
//                mapBackground.setLock(LockDirection.ALL);
////                playerStill.setLock(true);
//            }
//
//        }
        itemSpawner.update();
        enemySpawner.update();
        mapBorder.update();


        // Adjust for map movement
//        borderEntityLeft.moveBy(DataContainer.mapMovement.x, DataContainer.mapMovement.y);
//        borderEntityLeft.drawNextSprite();
//        Log.d("Map", "Tree pos: " + borderEntityLeft.getPosition().x + ", " + borderEntityLeft.getPosition().y + ". Status ");

    }

    public void move(float deltaX, float deltaY){
        mapBackground.moveFrame(deltaX, deltaY);
    }


    public void move(Entity player, Direction direction) {

        if (direction.getVelocity() != 0) {


            direction.lockInside(boarderInder, player.getRect().centerX(), player.getRect().centerY());
//            mapDirektion.tranfareWithRatio(player.move(direction));
            mapDirektion.tranfareWithRatio(direction);
            mapDirektion.lockInside(outerBorader, player.getPosition().x, player.getPosition().y);


            switch (mapDirektion.lock) {
                case X:
//                    Log.d("Map", "X");
//                    player.getPosition().y += direction.velocity_Y;
//                    this.playerStill.placeAt(this.playerStill.getPosition().x, this.playerStill.getPosition().y-direction.velocity_Y);
                    break;
                case Y:
//                    Log.d("Map", "Y");
//                    player.getPosition().x += direction.velocity_X;
//                    this.playerStill.placeAt(this.playerStill.getPosition().x-direction.velocity_X, this.playerStill.getPosition().y);
                    break;
                case UNLOCK:
//                    Log.d("Map", "UNLOCK");
//                    this.playerStill.placeAt(this.playerStill.getPosition().x-direction.velocity_X, this.playerStill.getPosition().y-direction.velocity_Y);
                    break;
                case ALL:
//                    Log.d("Map", "ALL");
//                    player.getPosition().x += direction.velocity_X;
//                    player.getPosition().y += direction.velocity_Y;
                    break;
            }
//            Direction mapd = mapBackground.moveFrame(mapDirektion);

        }


    }

}

























