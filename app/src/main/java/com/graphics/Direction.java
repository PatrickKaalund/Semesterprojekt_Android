package com.graphics;

/**
 * Created by thor on 11/4/16.
 */

public class Direction {
    private float velocity;
    private int angle;
    private int lastActiveAngle;

    public void set(int angle, float velocity) {
        this.velocity = velocity;
        if (velocity > 0) {
            this.lastActiveAngle = this.angle = angle;
        } else {
            this.angle = lastActiveAngle;
        }
    }


    public int getAngle() {
        return angle;
    }

    public float getVelocity() {
        return velocity;
    }


    public float rad() {
        return (float) Math.toRadians((double) angle);
    }


}
