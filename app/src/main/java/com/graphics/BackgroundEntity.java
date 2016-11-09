package com.graphics;

import android.graphics.Matrix;
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
    float ratio;

    protected BackgroundEntity(float baseHeight, float baseWidth) {
        this.baseHeight = baseHeight;
        this.baseWidth = baseWidth;
        baseRact = new RectF(0, baseHeight, baseWidth, 0);
        ratio = (float) baseHeight / (float) baseWidth;

        this.uvs = new RectF(0f, 0f, 1f, ratio);
        mustDrawThis(true);
        Log.d("BackgroundEntity", "baseHeight: " + baseHeight + " baseWidth: " + baseWidth + " baseRatio: " + ratio);
        Log.d("BackgroundEntity", "BaseRect: " + rectToString(baseRact));
        Log.d("BackgroundEntity", "uvs: " + rectToString(uvs));
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

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public void moveFrame(Direction direction) {
        if (lock) {
            return;
        }
        float cos = (float) Math.cos(direction.rad());
        float sin = (float) Math.sin(direction.rad());
        float velocity_X = (direction.getVelocity() * cos);
        float velocity_Y = direction.getVelocity() * sin;

        Matrix transformMatrix = new Matrix();
//        Log.w("BackgroundEntity", "Uvs before: " + rectToString(uvs));
        transformMatrix.setTranslate(velocity_X, -velocity_Y);
//        Log.w("BackgroundEntity", "Uvs after: " + rectToString(uvs));
        transformMatrix.mapRect(uvs);

    }
}
