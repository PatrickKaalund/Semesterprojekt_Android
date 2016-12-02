package com.graphics;

import android.graphics.PointF;
import android.graphics.RectF;


import java.util.ArrayList;

/**
 * Created by thor on 10/27/16.
 */

public interface Entity {

    void moveBy(float deltaX, float deltaY, float angle);

    void moveBy(float deltaX, float deltaY);

    void placeAt(float x, float y);

    void scale(float deltas);

    void rotate(float deltaa);

    void drawNextSprite();

    void setCurrentSprite(int currentSprite);

    int getCurrentSprite();

    void setAngleOffSet(float angleOffSet);

    void setAngle(float angle);

    RectF getRect();

    Direction move(Direction direction);

//    public void setLock(LockDirection lockDirection);

    void setAnimationDivider(int animationSpeed);

    void setAnimationOrder(int[] animationOrder);

    PointF getPosition();

    void setPosition(PointF position);

    boolean collision(float x, float y);

    boolean collision(PointF pos);

    boolean isHit();

    void setHit(boolean hit);

    void delete();

    boolean collisionBoarder(Entity in);

    Direction moveBy(Direction direction);

    void setAnimationOffset(int animationOffset);

    void setHitBoxSize(int width, int height);

    void mustDrawThis(boolean draw);

    int getNextSprite();
//    public void updateCurrentSprite();
}
