package com.graphics;

import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by thor on 10/22/16.
 */

public class GSprite {
    float angle;
    float scale;
    RectF baseRact;
    PointF currentPos;
    protected float[] uvs;//for texture

    public GSprite(RectF baseRact, PointF pos) {
        // Initialise our intital size around the 0,0 point
        this.baseRact = baseRact;

        // Initial Pos
        currentPos = pos;

        // We start with our inital size
        scale = 1f;

        // We start in our inital angle
        angle = 0f;
    }

    public GSprite(RectF baseRact, PointF pos,float[] uvs) {
        // Initialise our intital size around the 0,0 point
        this.baseRact = baseRact;

        // Initial Pos
        currentPos = pos;
        this.uvs = uvs;

        // We start with our inital size
        scale = 1f;

        // We start in our inital angle
        angle = 0f;
    }

    public GSprite() {
        this(new RectF(-50f, 50f, 50f, -50f), new PointF(50f, 50f));
    }


    public void moveBy(float deltaX, float deltaY) {
        // Update our location.
        currentPos.x += deltaX;
        currentPos.y += deltaY;
    }

    public void placeAt(float x, float y) {
        // Update our location.
        currentPos.x = x;
        currentPos.y = y;
    }

    public void scale(float deltas) {
        scale += deltas;
    }

    public void rotate(float deltaa) {
        angle += deltaa;
    }




    public float[] getUvs() {
        return uvs;
    }

    public void setUvs(float[] uvs) {
        this.uvs = uvs;
    }
}
