package com.gamelogic;

import android.graphics.PointF;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public abstract class PlayerCommon extends Creature {

    private SpriteEntityFactory playerFactory = new SpriteEntityFactory(R.drawable.players, 120, 120, 6, 11, new PointF(0, 0));
    protected Entity player;

    public PlayerCommon(){
        player = playerFactory.createEntity();
        player.setCurrentSprite(45);
        player.setAngleOffSet(0);
        player.setAnimationDivider(1);
        player.setAnimationOrder(new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64});
        super.speed = 5;
        super.health = 100;
    }



}
