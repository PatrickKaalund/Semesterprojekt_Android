package com.graphics;

import android.graphics.RectF;

import com.gamelogic.LockDirection;

/**
 * Created by thor on 10/30/16.
 */

abstract class GraphicEntity {
    protected RectF baseRact;
    protected boolean drawThis;
    protected LockDirection lockDirection = LockDirection.UNLOCK;

    abstract protected float[] getUvs();

    abstract protected float[] getModel();

    abstract public void mustDrawThis(boolean draw);

    abstract public boolean mustDrawThis();

    abstract public void setLock(LockDirection lockDirection);

//    protected GraphicEntity (float baseHeight, float baseWidth){
//        float width = baseWidth / 2;
//        float height = baseHeight / 2;
//        this.baseRact = new RectF(-width, height, width, -height);
//
//    }

}
