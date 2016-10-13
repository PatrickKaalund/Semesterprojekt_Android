package com.gamelogic;

import android.util.Log;

import com.core.UpdateAndDrawInterface;

public class Player extends Creature implements UpdateAndDrawInterface {

    public Player(){
        Log.d("Player","Player instantiated");

        super.speed = 1;
        super.health = 100;
        super.xPosition = 0;
        super.yPosition = 0;
    }

    @Override
    public void update(){
        Log.d("Player","Updating player...xPos: " + super.xPosition + ". yPos: "+ super.yPosition);
    }

    @Override
    public void draw() {

    }

}
