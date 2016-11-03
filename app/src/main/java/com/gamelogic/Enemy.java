package com.gamelogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.core.ScreenDrawer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Enemy extends Creature {

    private SpriteEntityFactory enemyFactory;
    private Entity enemy;

    public Enemy() {
        game.objectsToUpdate.add(this);

        super.speed = 1;
        super.health = 100;
        super.xPosition = 700;
        super.yPosition = 700;

        enemyFactory = new SpriteEntityFactory(R.drawable.zombie_topdown, 350, 350, 8, 36, new PointF(super.xPosition, super.yPosition));

        enemy = enemyFactory.createEntity();
        enemy.setCurrentSprite(4);

        enemy.rotate(-30);
    }

    @Override
    public void update() {

        // for testing :-)
        //enemy.moveBy(1, -1);


//        Log.d("Enemy", "Player rect: " + game.getPlayer().getPlayer().getRect());
//        Log.d("Enemy", "Enemy rect: " + enemy.getRect());
//
//        if(enemy.getRect().intersect(game.getPlayer().getPlayer().getRect())){
//            Log.d("Enemy", "You died!!!");
//        }
    }
}
