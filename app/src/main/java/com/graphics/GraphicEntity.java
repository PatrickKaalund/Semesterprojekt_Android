package com.graphics;

import android.graphics.RectF;

/**
 * Created by thor on 10/30/16.
 */

abstract class GraphicEntity {
    protected RectF baseRact;
    protected boolean drawThis;
    protected boolean lock = false;

    abstract protected float[] getUvs();

    abstract protected float[] getModel();

    abstract public void mustDrawThis(boolean draw);

    abstract public boolean mustDrawThis();

    abstract public boolean isLocked();

    abstract public void setLock(boolean lock);

//    protected GraphicEntity (float baseHeight, float baseWidth){
//        float width = baseWidth / 2;
//        float height = baseHeight / 2;
//        this.baseRact = new RectF(-width, height, width, -height);
//
//    }

}
