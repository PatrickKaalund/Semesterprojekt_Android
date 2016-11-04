package com.graphics;

import android.graphics.RectF;

/**
 * Created by thor on 10/27/16.
 */

public interface Entity {

    public void moveBy(float deltaX, float deltaY);

    public void placeAt(float x, float y);

    public void scale(float deltas);

    public void rotate(float deltaa);

    public void drawNextSprite();

    public void setCurrentSprite(int currentSprite);

    public int getCurrentSprite();

    public void setAngleOffSet(float angleOffSet);

    public RectF getRect();

    public RectF move(Direction direction);

    public boolean isLocked();

    public void setLock(boolean lock);
}
