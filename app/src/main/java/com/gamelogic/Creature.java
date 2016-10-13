package com.gamelogic;

import android.content.Context;

import com.core.GDrawable;
import com.core.ScreenDrawer;

public abstract class Creature extends GDrawable {
    public int speed;
    public int health;
    public int xPosition;
    public int yPosition;

    public Creature(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
    }

//    public Creature(){
//
//    }


}
