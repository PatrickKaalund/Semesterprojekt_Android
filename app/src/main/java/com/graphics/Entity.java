package com.graphics;

/**
 * Created by thor on 10/27/16.
 */

public interface Entity {

    public void moveBy(float deltaX, float deltaY);
    public void placeAt(float x, float y);
    public void scale(float deltas);
    public void rotate(float deltaa);
    public void setCurrentSprite(int currentSprite);
    public void drawNextSprite();
}
