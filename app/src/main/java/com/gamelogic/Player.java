package com.gamelogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.core.ScreenDrawer;
import com.example.patrickkaalund.semesterprojekt_android.R;

import java.util.ArrayList;

public class Player extends Creature {

    private ArrayList<Bitmap> bitmaps;
    private int bitmapToShow;
    private ArrayList<Integer> joystickValues;

    private int bitmapHeight;
    private int bitmapWidth;

    // temp solution
    private Rect mapL;
    private Rect mapR;
    private Rect mapU;
    private Rect mapD;

    private enum Direction {
        EAST, WEST, NORTH, SOUTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST
    }

    // animation
    private int numberOfDrawingWidth;

    public Player(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

        super.speed = 1;
        super.health = 100;
        super.xPosition = 100;
        super.yPosition = 500;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        float aspectRatio = bitmap.getWidth() /
                (float) bitmap.getHeight();
        int width = 1000;
        int height = Math.round(width / aspectRatio);

        bitmap = Bitmap.createScaledBitmap(
                bitmap, width, height, false);

        bitmaps = new ArrayList<>();

        numberOfDrawingWidth = 8;
        int numberOfDrawingHeight = 2;

        bitmapHeight = bitmap.getHeight() / numberOfDrawingHeight;
        bitmapWidth = bitmap.getWidth() / numberOfDrawingWidth;
//        Log.d("Player", "bitmapHeight: " + bitmapHeight);
//        Log.d("Player", "bitmapWidth: " + bitmapWidth);

        ArrayList<Bitmap> temp = new ArrayList<>();

        // loads animations.
        for (int i = 0; i < numberOfDrawingHeight; i++) {
            // load right animations when j = 0
            // load left animations when j = 1
            for (int j = 0; j < numberOfDrawingWidth; j++) {
                int startX = bitmapWidth * j;
                Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, startX, bitmapHeight * i, bitmapWidth, bitmapHeight);
                bitmaps.add(croppedBitmap);
            }
        }

        bitmapToShow = 0;

        joystickValues = new ArrayList<>();

        // drawing map bounds with 1 pixel width
        this.mapL = new Rect(0, 0, 1, screenDrawer.getScreenHeight());
        this.mapR = new Rect(screenDrawer.getScreenWidth() - 1, 0, screenDrawer.getScreenWidth(), screenDrawer.getScreenHeight());
        this.mapU = new Rect(0, 79, screenDrawer.getScreenWidth(), 80);
        this.mapD = new Rect(0, screenDrawer.getScreenHeight() - 1, screenDrawer.getScreenWidth(), screenDrawer.getScreenHeight());
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

    @Override
    public void draw(Canvas canvas) {

        // for debugging:
//        canvas.drawRect(mapR, screenDrawer.getPaint());
//        canvas.drawRect(mapL, screenDrawer.getPaint());
//        canvas.drawRect(mapU, screenDrawer.getPaint());
//        canvas.drawRect(mapD, screenDrawer.getPaint());
        canvas.drawRect(super.collisionBox, screenDrawer.getPaint());
        // --------//

        canvas.drawBitmap(bitmaps.get(bitmapToShow), xPosition, yPosition, null);
    }

    private void updatePlayer(int joystick_angle, int joystick_strength) {

        // stand still
        if (joystick_strength == 0) {
            // do nothing
        } else {
            Direction myDirection = calculateWalkingDirection(joystick_angle);
            myDirection = calculateWalkingDirection(joystick_angle);
            animate(myDirection);
            checkCollisionAndMove(myDirection, joystick_strength);

        }

        super.collisionBox.set(xPosition, yPosition, xPosition + bitmapWidth, yPosition + bitmapHeight);
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

    private void animate(Direction direction) {
        switch (direction) {
            case EAST:
            case NORTH_EAST:
            case SOUTH_EAST:
                if (bitmapToShow + 1 > numberOfDrawingWidth - 1) {
                    bitmapToShow = 0;
                } else {
                    bitmapToShow++;
                }
                break;
            case WEST:
            case NORTH_WEST:
            case SOUTH_WEST:
                if (bitmapToShow + 1 >= bitmaps.size() || bitmapToShow < numberOfDrawingWidth) {
                    bitmapToShow = numberOfDrawingWidth;
                } else {
                    bitmapToShow++;
                }
                break;
            default:
//                Log.d("Player", "Animate: Unknown direction");
        }
    }

    private void checkCollisionAndMove(Direction direction, int joystick_strength) {
        switch (direction) {
            case EAST:
                if (super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.xPosition += joystick_strength;
                }
                break;
            case WEST:
                if (super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.xPosition -= joystick_strength;
                }
                break;
            case NORTH:
                if (super.collisionBox.intersect(mapU)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.yPosition -= joystick_strength;
                }
                break;
            case SOUTH:
                if (super.collisionBox.intersect(mapD)) {
                    Log.d("Player", "Colliding!!!");
                } else {
                    super.yPosition += joystick_strength;
                }
                break;
            case NORTH_EAST:
                if (super.collisionBox.intersect(mapU) && super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapU)) {
                    super.xPosition += joystick_strength;
                } else if (super.collisionBox.intersect(mapR)) {
                    super.yPosition -= joystick_strength;
                } else {
                    super.xPosition += joystick_strength;
                    super.yPosition -= joystick_strength;
                }
                break;
            case NORTH_WEST:
                if (super.collisionBox.intersect(mapU) && super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapU)) {
                    super.xPosition -= joystick_strength;
                } else if (super.collisionBox.intersect(mapL)) {
                    super.yPosition -= joystick_strength;
                } else {
                    super.xPosition -= joystick_strength;
                    super.yPosition -= joystick_strength;
                }
                break;
            case SOUTH_EAST:
                if (super.collisionBox.intersect(mapD) && super.collisionBox.intersect(mapR)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapD)) {
                    super.xPosition += joystick_strength;
                } else if (super.collisionBox.intersect(mapR)) {
                    super.yPosition += joystick_strength;
                } else {
                    super.xPosition += joystick_strength;
                    super.yPosition += joystick_strength;
                }
                break;
            case SOUTH_WEST:
                if (super.collisionBox.intersect(mapD) && super.collisionBox.intersect(mapL)) {
                    Log.d("Player", "Colliding!!!");
                } else if (super.collisionBox.intersect(mapD)) {
                    super.xPosition -= joystick_strength;
                } else if (super.collisionBox.intersect(mapL)) {
                    super.yPosition += joystick_strength;
                } else {
                    super.xPosition -= joystick_strength;
                    super.yPosition += joystick_strength;
                }
                break;
            default:
                Log.d("Player", "isColliding: Unknown direction");
        }
    }
}
