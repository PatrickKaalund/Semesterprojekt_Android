package com.gamelogic;

import android.content.Context;
import android.graphics.Rect;

import com.core.GDrawable;
import com.core.GUpdateable;
import com.core.ScreenDrawer;

public abstract class Creature extends GUpdateable {
    public int speed;
    public int health;

    public Creature() {

    }
    public abstract boolean doDamage(int damage);
}
