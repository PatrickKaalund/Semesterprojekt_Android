package com.graphics;

import android.graphics.RectF;
import android.widget.Switch;

import java.util.concurrent.locks.Lock;

import static com.graphics.GraphicsTools.LL;

/**
 * Created by thor on 11/4/16.
 */

public class Direction {
    private float velocity;
    private int angle;
    private int lastActiveAngle;
    public float velocity_X, velocity_Y;
    public Lock_e lock = Lock_e.UNLOCK;

    public enum Lock_e {
        X,
        Y,
        UNLOCK,
        ALL;

    }

    Edge EGDE_X = Edge.NONE;
    Edge EGDE_Y = Edge.NONE;

    public enum Edge {
        LEFT,
        RIGHT,
        TOP,
        BOTTUM,
        NONE
    }

    private void checkX(RectF r, float x) {

        if (x <= r.left) {
            this.EGDE_X = Edge.LEFT;
        } else if (x >= r.right) {
            this.EGDE_X = Edge.RIGHT;
            LL(this, "lock X " + x + " r.l " + r.left + " r.r " + r.right);
        }
    }

    private void checkY(RectF r, float y) {

        if (y >= r.bottom) {
            this.EGDE_Y = Edge.BOTTUM;
        } else if (y <= r.top) {
            this.EGDE_Y = Edge.TOP;
            LL(this, "lock Y " + y + " r.bot " + r.bottom + " r.top " + r.top);
        }
    }


    public void lockInside(RectF r, float x, float y) {

        switch (lock) {
            case X:
                if (EGDE_X == Edge.RIGHT && angle > 90 && angle < 270) lock = Lock_e.UNLOCK;
                else if (EGDE_X == Edge.LEFT && angle < 90 && angle > 270) lock = Lock_e.UNLOCK;
                break;
            case Y:
                if (EGDE_Y == Edge.TOP && angle > 0 && angle < 180) lock = Lock_e.UNLOCK;
                else if (EGDE_X == Edge.BOTTUM && angle < 0 && angle > 180) lock = Lock_e.UNLOCK;
                break;
            case UNLOCK:
                checkX(r, x);
                checkY(r, y);
                if (EGDE_X != Edge.NONE && EGDE_Y != Edge.NONE) lock = Lock_e.ALL;
                else if (EGDE_X != Edge.NONE) lock = Lock_e.X;
                else if (EGDE_Y != Edge.NONE) lock = Lock_e.Y;
                else lock = Lock_e.UNLOCK;
                break;
            case ALL:
                break;
        }


//
//        this.lock = Lock_e.UNLOCK;
//        this.lock.EGDE_X = Lock_e.Edge.NONE;
//        this.lock.EGDE_Y = Lock_e.Edge.NONE;
//        LL(this, "lock UNLOCK: X--> " + x + " y--> " + y);


    }

    public float calcVelocity_Y() {
        velocity_Y = velocity * (float) Math.sin((float) Math.toRadians((double) angle));
        return velocity_Y;
    }

    public float calcVelocity_X() {
        velocity_X = velocity * (float) Math.cos((float) Math.toRadians((double) angle));
        return velocity_X;
    }

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
