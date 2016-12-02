package com.graphics;

public class Direction {

    public int tag;
    private float velocity;
    private int angle;
    public float velocity_X, velocity_Y;
    public int baseSpeed = 1;

    /**
     * Direction for objects in the game
     *
     * @param baseSpeed
     */
    public Direction(int baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public Direction() {
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public Direction(Direction other) {
        this.velocity = other.velocity;
        this.angle = other.angle;
        this.velocity_X = other.velocity_X;
        this.velocity_Y = other.velocity_Y;
        this.baseSpeed = other.baseSpeed;

    }

    /**
     * Copy constructor to change speed
     *
     * @param other
     */
    public Direction(Direction other, int baseSpeed) {
        this.velocity = other.velocity;
        this.angle = other.angle;
        this.velocity_X = other.velocity_X;
        this.velocity_Y = other.velocity_Y;
        this.baseSpeed = baseSpeed;
    }

    /**
     * Calc only once
     */
    private void calcVelocity() {
        velocity_X = velocity * (float) Math.cos((float) Math.toRadians((double) angle));
        velocity_Y = velocity * (float) Math.sin((float) Math.toRadians((double) angle));
    }

    /**
     * Set a direction. Will calculate x and y deltas based on angle and velocity
     *
     * @param angle
     * @param velocity
     */
    public void set(int angle, float velocity) {
        this.velocity = velocity / baseSpeed;
        if (velocity > 0) {
            this.angle = angle;
            calcVelocity();
        } else {
            velocity_X = 0;
            velocity_Y = 0;
        }
    }

    public void set(float velocityX, float velocityY){
        this.velocity_X = velocityX;
        this.velocity_Y = velocityY;
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


    @Override
    public String toString() {
        return "ID " + Integer.toHexString(hashCode()) + ". Val --> " +
                "velocity" + '[' + velocity + ']' +
                "angle" + '[' + angle + ']' +
                "velocity_X" + '[' + velocity_X + ']' +
                "velocity_Y" + '[' + velocity_Y + ']' +
                "baseSpeed" + '[' + baseSpeed + ']';
    }


}
