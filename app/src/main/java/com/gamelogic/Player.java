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

    private Rect mapL;
    private Rect mapR;

    // animation
    private int numberOfDrawingWidth;

    public Player(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

//        Log.d("Player", "Player instantiated");

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

        // stand still
        if (joystick_strength == 0) {

        }
        // walk right
        else if (joystick_angle < 90 || joystick_angle > 270) {
            // calc animation
            if (bitmapToShow + 1 > numberOfDrawingWidth - 1) {
                bitmapToShow = 0;
            } else {
                bitmapToShow++;
            }

            // move player
            if (super.collisionBox.intersect(mapR)) {
                Log.d("Player", "Colliding!!");
            } else {
                super.xPosition += joystick_strength;
            }
        }
        // walk left
        else {
            // calc animation
            if (bitmapToShow + 1 >= bitmaps.size() || bitmapToShow < numberOfDrawingWidth) {
                bitmapToShow = numberOfDrawingWidth;
            } else {
                bitmapToShow++;
            }

            // move player
            if (super.collisionBox.intersect(mapL)) {
                Log.d("Player", "Colliding!!");
            } else {
                super.xPosition -= joystick_strength;
            }
        }

        super.collisionBox.set(xPosition, yPosition, xPosition+bitmapWidth, yPosition+bitmapHeight);
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

    public void setPlayerPosition(int xPos, int yPos) {
        this.xPosition = xPos;
        this.yPosition = yPos;
    }
}
