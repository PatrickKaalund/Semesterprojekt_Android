package com.gamelogic;

import android.graphics.PointF;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public class PlayerCommon extends Creature {

    private SpriteEntityFactory playerFactory = new SpriteEntityFactory(R.drawable.soldier_topdown_adjusted, 200, 200, 4, 2, new PointF(400, 400));
    protected Entity player;
    protected Direction direction;

    public PlayerCommon(){
        player = playerFactory.createEntity();
        player.setCurrentSprite(0);
        player.setAngleOffSet(90);
        player.setAnimationDivider(10);
        player.setAnimationOrder(new int[]{0, 1, 2, 3, 4});
        super.speed = 5;
        direction = new Direction(super.speed, 200);

        super.health = 100;
    }

    @Override
    public void update() {

    }
}
