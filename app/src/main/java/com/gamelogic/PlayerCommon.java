package com.gamelogic;

import android.graphics.PointF;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;


abstract class PlayerCommon extends Creature {

    protected Entity player;
    int lives;
    public String name;
    static final int LIVES_TOTAL = 3;
    static final int BASE_HEALTH = 100;

    PlayerCommon() {
        SpriteEntityFactory playerFactory = new SpriteEntityFactory(R.drawable.players_red_small, 120, 200, 12, 11, new PointF(0, 0));
        player = playerFactory.createEntity();
        player.setHitBoxSize(120, 120);

        super.speed = 5;
        super.health = BASE_HEALTH;
        lives = LIVES_TOTAL;
    }
}
