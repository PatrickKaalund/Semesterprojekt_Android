package com.gamelogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.core.ScreenDrawer;
import com.example.patrickkaalund.semesterprojekt_android.R;

import java.util.ArrayList;

public class Player extends Creature {

    private ArrayList<Bitmap> bitmaps;
    private Bitmap bitmap;
    private Paint paint;
    private int bitmapToShow;

    public Player(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

//        Log.d("Player", "Player instantiated");

        super.speed = 1;
        super.health = 100;
        super.xPosition = 100;
        super.yPosition = 1080;

        paint = new Paint();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);

        float aspectRatio = bitmap.getWidth() /
                (float) bitmap.getHeight();
        int width = 1000;
        int height = Math.round(width / aspectRatio);

        bitmap = Bitmap.createScaledBitmap(
                bitmap, width, height, false);

        bitmaps = new ArrayList<>();

//        int numberOfWidth

        int bitmapHeight = bitmap.getHeight() / 2;
        int bitmapWidth = bitmap.getWidth() / 8;
//        Log.d("Player", "bitmapHeight: " + bitmapHeight);
//        Log.d("Player", "bitmapWidth: " + bitmapWidth);

        ArrayList<Bitmap> temp = new ArrayList<>();

        // load right animations
        for (int i = 0; i < 8; i++) {
            int startX = bitmapWidth * i;
            Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, startX, 0, bitmapWidth, bitmapHeight);
            bitmaps.add(croppedBitmap);
        }

        bitmapToShow = 0;

    }


    @Override
    public void update() {
        if (super.xPosition % 10 == 0) {
//            Log.d("Player", "Changed animation index");
            if (bitmapToShow + 1 < bitmaps.size()) {
                bitmapToShow++;
            } else {
                bitmapToShow = 0;
            }
        }

        if (xPosition < 900) {
            super.xPosition += 10;
        } else {
            super.xPosition = 100;
        }

        //Log.d("Player","Updating player...xPos: " + super.xPosition + ". yPos: "+ super.yPosition);
    }

    @Override
    public void draw(Canvas canvas) {

        //Log.d("Player","drawing player");

        paint.setColor(Color.BLUE);


        canvas.drawBitmap(bitmaps.get(bitmapToShow), xPosition, yPosition, null);

    }

    public void setPlayerPosition(int xPos, int yPos) {
        this.xPosition = xPos;
        this.yPosition = yPos;
    }
}
