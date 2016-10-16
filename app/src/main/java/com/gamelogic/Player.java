package com.gamelogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

    private enum Direction {
        RIGHT, LEFT
    };

    // animation
    private int numberOfDrawingWidth;

    public Player(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

        super.speed = 1;
        super.health = 100;
        super.xPosition = 100;
        super.yPosition = 1000;

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
        canvas.drawRect(mapR, screenDrawer.getPaint());
        canvas.drawRect(mapL, screenDrawer.getPaint());
        canvas.drawRect(super.collisionBox, screenDrawer.getPaint());
        // --------//

        canvas.drawBitmap(bitmaps.get(bitmapToShow), xPosition, yPosition, null);
    }

    private void updatePlayer(int joystick_angle, int joystick_strength){
        // stand still
        if (joystick_strength == 0) {

        }
        // walk right
        else if (joystick_angle < 90 || joystick_angle > 270) {
            // calc animation
            animate(Direction.RIGHT);

            // check collision, and move player
            if (isColliding(Direction.RIGHT)) {
                Log.d("Player", "Colliding!!");
            } else {
                super.xPosition += joystick_strength;
            }
        }
        // walk left
        else {
            // calc animation
            animate(Direction.LEFT);

            // check collision, and move player
            if (isColliding(Direction.LEFT)) {
                Log.d("Player", "Colliding!!");
            } else {
                super.xPosition -= joystick_strength;
            }
        }

        super.collisionBox.set(xPosition, yPosition, xPosition+bitmapWidth, yPosition+bitmapHeight);
    }

    private void animate(Direction direction){
        switch (direction){
            case RIGHT:
                if (bitmapToShow + 1 > numberOfDrawingWidth - 1) {
                    bitmapToShow = 0;
                } else {
                    bitmapToShow++;
                }
                break;
            case LEFT:
                if (bitmapToShow + 1 >= bitmaps.size() || bitmapToShow < numberOfDrawingWidth) {
                    bitmapToShow = numberOfDrawingWidth;
                } else {
                    bitmapToShow++;
                }
                break;
            default:
                Log.d("Player", "Animate: Unknown direction");
        }
    }

    private boolean isColliding(Direction direction){
        switch (direction){
            case RIGHT:
                if(super.collisionBox.intersect(mapR)){
                    return true;
                }
                break;
            case LEFT:
                if(super.collisionBox.intersect(mapL)){
                    return true;
                }
                break;
            default:
                Log.d("Player", "isColliding: Unknown direction");
        }
        return false;
    }
}
