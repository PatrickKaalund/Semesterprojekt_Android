package core;


import models.Player;

public class Game implements UpdateInterface {

    private Player player;

    public Game(){
        player = new Player();

    }

    @Override
    public void update() {
        player.update();
    }
}
