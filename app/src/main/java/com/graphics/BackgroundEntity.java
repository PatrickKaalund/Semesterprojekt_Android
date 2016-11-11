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

//    @Override
//    public void setLock(LockDirection lockDirection) {
//        this.lockDirection = lockDirection;
//    }

    public void moveFrame(Direction direction, float devider) {
        Matrix transformationMatrix = new Matrix();
        switch (direction.lock) {
            case X:
                transformationMatrix.setTranslate(0, -direction.velocity_Y/devider);
                break;
            case Y:
                transformationMatrix.setTranslate(direction.velocity_X/devider, 0);
                break;
            case UNLOCK:
                transformationMatrix.setTranslate(direction.velocity_X/devider, -direction.velocity_Y/devider);
                break;
            case ALL:
                break;
        }
//        Log.w("BackgroundEntity", "Uvs before: " + rectToString(uvs));
//        Log.w("BackgroundEntity", "Uvs after: " + rectToString(uvs));
        transformationMatrix.mapRect(uvs);
    }
}
