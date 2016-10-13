package com.core;

import android.util.Log;

public class GameStateHandler implements UpdateAndDrawInterface {

    private enum States {INITIALISE, IN_GAME}
    private States state;

    private Game game;

    public GameStateHandler(){
        this.state = States.INITIALISE;
        this.game = new Game();

        // for now! should be set from the 'begin game in menu' click etc.
        this.setState(States.IN_GAME);
    }

    public void update(){
        switch(state){
            case INITIALISE:
                //Log.d("GameStateHandler","INITIALISE");
                break;
            case IN_GAME:
                //Log.d("GameStateHandler","INITIALISE");
                game.update();
                break;
        }
    }

    @Override
    public void draw() {

    }

    public void setState(States newState){
        state = newState;
        Log.d("GameStateHandler","Changed state: " + newState);
    }

    public States getState(){
        return this.state;
    }
}
