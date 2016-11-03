package com.gamelogic;

import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends Creature {

    private SpriteEntityFactory playerFactory;
    private Entity player;

    private ArrayList<Integer> joystickValues;

    // temp solution
    private Rect mapL;
    private Rect mapR;
    private Rect mapU;
    private Rect mapD;

    private int animationCounter = 0;

    private enum Direction {
        EAST, WEST, NORTH, SOUTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }

    public Player() {
        game.objectsToUpdate.add(this);

        super.speed = 1;
        super.health = 100;
        super.xPosition = 400;
        super.yPosition = 400;

        playerFactory = new SpriteEntityFactory(R.drawable.soldier_topdown_adjusted, 200, 200, 4, 2, new PointF(super.xPosition, super.yPosition));

        player = playerFactory.createEntity();
        player.setCurrentSprite(0);

        joystickValues = new ArrayList<>();

        // drawing map bounds with 1 pixel width
        this.mapL = new Rect(0, 0, 1, 1776);
        this.mapR = new Rect(1080 - 1, 0, 1080, 1776);
        this.mapU = new Rect(0, 79, 1080, 80);
        this.mapD = new Rect(0, 1776 - 1, 1080, 1776);
    }

    @Override
    public void update() {

        // read joystick
        joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        int joystick_strength = (joystickValues.get(1) / 10);

//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);

        updatePlayer(joystick_angle, joystick_strength);

    }


    private void updatePlayer(int joystick_angle, int joystick_strength) {

        // stand still
        if (joystick_strength == 0) {
            // do nothing
            // animate standing movement :-)
        } else {
            Direction myDirection = calculateWalkingDirection(joystick_angle);
            animate(myDirection, joystick_angle);
//            checkCollisionAndMove(myDirection, joystick_strength);

        }

//        super.collisionBox.set(xPosition, yPosition, xPosition + bitmapWidth, yPosition + bitmapHeight);
    }

    private Direction calculateWalkingDirection(int joystick_angle) {
        if (joystick_angle < 23 || joystick_angle > 338) {
            return Direction.EAST;
        } else if (joystick_angle >= 23 && joystick_angle < 68) {
            return Direction.NORTH_EAST;
        } else if (joystick_angle >= 68 && joystick_angle < 113) {
            return Direction.NORTH;
        } else if (joystick_angle >= 113 && joystick_angle < 158) {
            return Direction.NORTH_WEST;
        } else if (joystick_angle >= 158 && joystick_angle < 203) {
            return Direction.WEST;
        } else if (joystick_angle >= 203 && joystick_angle < 248) {
            return Direction.SOUTH_WEST;
        } else if (joystick_angle >= 248 && joystick_angle < 293) {
            return Direction.SOUTH;
        } else {
            return Direction.SOUTH_EAST;
        }
    }

    private void animate(Direction direction, int joystick_angle) {
//        player.rotate(joystick_angle);
//        Log.d("Player", "Current sprite ID: !!! " + player.getCurrentSprite());

        Log.d("Player", "Animation angle: " + joystick_angle);
        player.setAngle(joystick_angle);

        if((++animationCounter % 3) == 0){
            animationCounter = 0;
            if(player.getCurrentSprite() < 5){
                player.drawNextSprite();
            }else{
                player.setCurrentSprite(0);
            }
        }
    }

    private void checkCollisionAndMove(Direction direction, int joystick_strength) {
        switch (direction) {
            case EAST:
                if (super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.xPosition += joystick_strength;
                    player.moveBy(5f, 0f);
                }
                break;
            case WEST:
                if (super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.xPosition -= joystick_strength;
                    player.moveBy(-5f, 0f);
                }
                break;
            case NORTH:
                if (super.collisionBox.intersect(mapU)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.yPosition -= joystick_strength;
                    player.moveBy(0f, 5f);
                }
                break;
            case SOUTH:
                if (super.collisionBox.intersect(mapD)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.yPosition += joystick_strength;
                    player.moveBy(0f, -5f);
                }
                break;
            case NORTH_EAST:
                if (super.collisionBox.intersect(mapU) && super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapU)) {
                    super.xPosition += joystick_strength;
                    player.moveBy(5f, 0f);
                } else if (super.collisionBox.intersect(mapR)) {
                    super.yPosition -= joystick_strength;
                    player.moveBy(0f, -5f);
                } else {
                    super.xPosition += joystick_strength;
                    super.yPosition -= joystick_strength;
                    player.moveBy(5f, 5f);
                }
                break;
            case NORTH_WEST:
                if (super.collisionBox.intersect(mapU) && super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapU)) {
                    super.xPosition -= joystick_strength;
                    player.moveBy(-5f, 0f);
                } else if (super.collisionBox.intersect(mapL)) {
                    super.yPosition -= joystick_strength;
                    player.moveBy(0f, -5f);
                } else {
                    super.xPosition -= joystick_strength;
                    super.yPosition -= joystick_strength;
                    player.moveBy(-5f, 5f);
                }
                break;
            case SOUTH_EAST:
                if (super.collisionBox.intersect(mapD) && super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapD)) {
                    super.xPosition += joystick_strength;
                    player.moveBy(5f, 0f);
                } else if (super.collisionBox.intersect(mapR)) {
                    super.yPosition += joystick_strength;
                    player.moveBy(0f, 5f);
                } else {
                    super.xPosition += joystick_strength;
                    super.yPosition += joystick_strength;
                    player.moveBy(5f, -5f);
                }
                break;
            case SOUTH_WEST:
                if (super.collisionBox.intersect(mapD) && super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapD)) {
                    super.xPosition -= joystick_strength;
                    player.moveBy(-5f, 0f);
                } else if (super.collisionBox.intersect(mapL)) {
                    super.yPosition += joystick_strength;
                    player.moveBy(0f, 5f);
                } else {
                    super.xPosition -= joystick_strength;
                    super.yPosition += joystick_strength;
                    player.moveBy(-5f, -5f);
                }
                break;
            default:
                Log.d("Player", "isColliding: Unknown direction");
        }
    }

    public Entity getPlayer(){ return this.player; }
}
