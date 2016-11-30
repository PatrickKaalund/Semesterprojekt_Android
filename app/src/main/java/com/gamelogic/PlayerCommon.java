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

    private SpriteEntityFactory playerFactory = new SpriteEntityFactory(R.drawable.players_red_small, 120, 120, 12, 11, new PointF(0, 0));
    protected Entity player;
    public int lives;

    public PlayerCommon() {
        player = playerFactory.createEntity();
        player.setHitBoxSize(120, 120);

        player.setAngleOffSet(0);
        player.setAnimationDivider(1);
        player.setAnimationOrder(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

        super.speed = 5;
        super.health = 100;
        lives = 3;
    }


}
