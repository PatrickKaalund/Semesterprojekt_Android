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

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Enemy extends Creature {
    @Override
    public void update() {

    }

//    public Enemy(Context context, ScreenDrawer screenDrawer) {
//        super(context, screenDrawer);
//        screenDrawer.objectsToDraw.add(this);
//        Log.d("Player","Player instantiated");
//
//        super.speed = 1;
//        super.health = 100;
//        super.xPosition = 100;
//        super.yPosition = 1580;
//    }
//
//    @Override
//    public void update(){
//        if(xPosition < 100){
//            super.xPosition -= 2;
//        }else{
//            super.xPosition = 1000;
//        }
//
//        //Log.d("Player","Updating player...xPos: " + super.xPosition + ". yPos: "+ super.yPosition);
//    }
//
//    @Override
//    public void draw(Canvas canvas) {
//
//        //Log.d("Player","drawing player");
//
//        Paint paint = new Paint();
//
//        paint.setColor(Color.BLUE);
//
//        canvas.drawCircle(xPosition, yPosition, 15, paint);
//
//        paint.setTextSize(40);
//        canvas.drawText("Player", xPosition - 60, yPosition + 60, paint);
//
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
//        canvas.drawBitmap(bitmap, 0, 0, null);
//
//    }
//
//    public void setPlayerPosition(int xPos, int yPos){
//        this.xPosition = xPos;
//        this.yPosition = yPos;
//    }
}
