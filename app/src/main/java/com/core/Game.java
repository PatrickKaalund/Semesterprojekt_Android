package com.core;


import android.graphics.Canvas;

import com.gamelogic.Player;

public class Game implements UpdateAndDrawInterface {

    private Player player;

    public Game(){
        player = new Player();

    }

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
