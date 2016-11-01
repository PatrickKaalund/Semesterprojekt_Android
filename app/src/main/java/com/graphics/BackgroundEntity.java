package com.graphics;

import android.graphics.RectF;
import android.util.Log;

import static com.graphics.GraphicsTools.getCornersFromRect;
import static com.graphics.GraphicsTools.getCornersFromRectWithZ;
import static com.graphics.GraphicsTools.rectToString;

/**
 * Created by thor on 10/30/16.
 */

public class BackgroundEntity extends GraphicEntity {
    protected RectF uvs;
    private float baseHeight;
    private float baseWidth;

    protected BackgroundEntity(float baseHeight, float baseWidth) {
        this.baseHeight = baseHeight;
        this.baseWidth = baseWidth;
        baseRact = new RectF(0,baseHeight,baseWidth,0);

        this.uvs = new RectF(0f,0f,.25f,.25f);
        mustDrawThis(true);
        Log.d("BackgroundEntity","baseHeight: "+baseHeight+" baseWidth: "+baseWidth);
        Log.d("BackgroundEntity","BaseRect: "+rectToString(baseRact));
        Log.d("BackgroundEntity","uvs: "+rectToString(uvs));
    }

    @Override
    protected float[] getUvs() {
        return getCornersFromRect(uvs);
    }

    @Override
    protected float[] getModel() {
         return getCornersFromRectWithZ(baseRact);
    }

    @Override
    public void mustDrawThis(boolean draw) {
        drawThis = draw;
    }

    @Override
    public boolean mustDrawThis() {
        return drawThis;
    }

    public void moveFrame(){

    }
}
