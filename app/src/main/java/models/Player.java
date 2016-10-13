package models;

import android.util.Log;

import core.UpdateInterface;

/**
 *  Created by ??? on 07/10/2016.
 */

public class Player implements UpdateInterface {


    public Player(){
        Log.d("Player","Player instantiated");
    }

    @Override
    public void update(){
        Log.d("Player","Updating player...");
    }

}
