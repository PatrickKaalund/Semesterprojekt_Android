package com.graphics;

import android.graphics.RectF;

import static com.graphics.GraphicsTools.LL;

/**
 * Created by thor on 11/4/16.
 */

public class Direction {
    public static final int UNLOCK = 0;
    public static final int X = 1;
    public static final int Y = 2;
    public static final int ALL = 3;
    public static final int SETY = 1;
    public static final int SETX = 0;
    private float divider;
    private float velocity;
    private int angle;
    public float velocity_X, velocity_Y;
    public int lock = UNLOCK;
    public int baseSpeed = 1;
    public int objectSize = 1;


    Edge EGDE_X = Edge.NONE;
    Edge EGDE_Y = Edge.NONE;

    public Direction(int baseSpeed, int objectSize) {
        this.baseSpeed = baseSpeed;
        this.objectSize = objectSize;
        this.divider = 1f / (objectSize * baseSpeed);
    }

    public Direction() {

    }

    public Direction(Direction other) {
        this.divider = other.divider;
        this.velocity = other.velocity;
        this.angle = other.angle;
        this.velocity_X = other.velocity_X;
        this.velocity_Y = other.velocity_Y;
        this.lock = other.lock;
        this.baseSpeed = other.baseSpeed;
        this.objectSize = other.objectSize;
        this.EGDE_X = other.EGDE_X;
        this.EGDE_Y = other.EGDE_Y;
    }

    public Direction(Direction other, int baseSpeed) {
        this.divider = other.divider;
        this.velocity = other.velocity;
        this.angle = other.angle;
        this.velocity_X = other.velocity_X;
        this.velocity_Y = other.velocity_Y;
        this.lock = other.lock;
        this.baseSpeed = baseSpeed;
        this.objectSize = other.objectSize;
        this.EGDE_X = other.EGDE_X;
        this.EGDE_Y = other.EGDE_Y;
    }

    public enum Edge {
        LEFT,
        RIGHT,
        TOP,
        BOTTUM,
        NONE
    }

    private int checkX(RectF r, float x) {

        if (x <= r.left) {
            this.EGDE_X = Edge.LEFT;
            return 1;
        } else if (x >= r.right) {
            this.EGDE_X = Edge.RIGHT;
//            LL(this, "lock X " + x + " r.l " + r.left + " r.r " + r.right);
            return 1;
        }
        return 0;
    }

    private int checkY(RectF r, float y) {

        if (y >= r.bottom) {
            this.EGDE_Y = Edge.BOTTUM;
            return 1;
        } else if (y <= r.top) {
            this.EGDE_Y = Edge.TOP;
//            LL(this, "lock Y " + y + " r.bot " + r.bottom + " r.top " + r.top);
            return 1;
        }
        return 0;
    }


    public void lockInside(RectF r, float x, float y) {
//        LL(this, "lockInside x " + x + " y " + y);
//        LL(this, "lockInside RectF " + " r.bot " + r.bottom + " r.top " + r.top + " r.l " + r.left + " r.r " + r.right);

//        LL(this, "lockInside Angle: " + angle + " edge x " + EGDE_X);
//        LL(this, "lockInside eval1: " + (EGDE_X == Edge.RIGHT && angle > 90 && angle < 270));
//        LL(this, "lockInside eval2: " + ((EGDE_X == Edge.LEFT && angle < 90 && angle > 270)));
        lock |= (checkX(r, x) << SETX);
        lock |= (checkY(r, y) << SETY);
//        LL(this, "lockInside lock1 : " + lock);


        switch (lock) {
            case UNLOCK:
//                LL(this, "lockInside unlock");
//                lock |= (checkX(r, x) << SETX);
//                lock |= (checkY(r, y) << SETY);
                break;
            case X:
                if ((EGDE_X == Edge.RIGHT && angle > 90 && angle < 270) || ((EGDE_X == Edge.LEFT && (angle < 90 || angle > 270)))) {
//                    LL(this, "lockInside x 1");
                    lock = UNLOCK;
                    EGDE_X = Edge.NONE;
                }
                break;
            case Y:
                if ((EGDE_Y == Edge.TOP && angle < 180) || (EGDE_Y == Edge.BOTTUM && angle > 180)) {
//                    LL(this, "lockInside y 1");
                    EGDE_Y = Edge.NONE;
                    lock = UNLOCK;
                }
                break;
            case ALL:
                if ((EGDE_X == Edge.RIGHT && angle > 90 && angle < 270) || ((EGDE_X == Edge.LEFT && (angle < 90 || angle > 270)))) {
//                    LL(this, "lockInside x 1");
                    lock = UNLOCK;
                    EGDE_X = Edge.NONE;
                }
                if ((EGDE_Y == Edge.TOP && angle < 180) || (EGDE_Y == Edge.BOTTUM && angle > 180)) {
//                    LL(this, "lockInside y 1");
                    EGDE_Y = Edge.NONE;
                    lock = UNLOCK;
                }
//                LL(this, "lockInside ALL");
                break;
        }
//        LL(this, "lockInside lock2 : " + lock);


    }

    public void tranfareWithRatio(Direction d) {
        velocity_Y = d.velocity_Y;// * d.divider;
        velocity_X = d.velocity_X;// * d.divider;
        angle = d.angle;
//        LL(this, "tranfareWithRatio lock before : " + lock);
        lock = ~(d.lock | 0xFFFFFFFC);
//        LL(this, "tranfareWithRatio lock after: " + lock);

    }



    private void calcVelocity() {
        velocity_X = velocity * (float) Math.cos((float) Math.toRadians((double) angle));
        velocity_Y = velocity * (float) Math.sin((float) Math.toRadians((double) angle));
    }


    public void set(int angle, float velocity) {
        this.velocity = velocity / baseSpeed;
        if (velocity > 0) {
            this.angle = angle;
            calcVelocity();
        }else{
            velocity_X = 0;
            velocity_Y = 0;
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
