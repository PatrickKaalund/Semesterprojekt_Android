package com.gamelogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.ImageReader;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;

import com.core.UpdateAndDrawInterface;
import com.example.patrickkaalund.semesterprojekt_android.R;

public class Player extends Creature implements UpdateAndDrawInterface {

    //private Image playerImage = ;

    public Player(){
        Log.d("Player","Player instantiated");


        super.speed = 1;
        super.health = 100;
        super.xPosition = 100;
        super.yPosition = 1080;
    }

    @Override
    public void update(){
        if(xPosition < 1000){
            super.xPosition += 2;
        }else{
            super.xPosition = 100;
        }

        //Log.d("Player","Updating player...xPos: " + super.xPosition + ". yPos: "+ super.yPosition);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, Context context) {

        //Log.d("Player","drawing player");

        paint.setColor(Color.BLUE);

        canvas.drawCircle(xPosition, yPosition, 15, paint);

        paint.setTextSize(40);
        canvas.drawText("Player", xPosition - 60, yPosition + 60, paint);

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    public void setPlayerPosition(int xPos, int yPos){
        this.xPosition = xPos;
        this.yPosition = yPosition;
    }
}
