package com.core;


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
    public void draw() {

    }
}
