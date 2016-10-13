package com.gamelogic;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.core.UpdateAndDrawInterface;

public class Player extends Creature implements UpdateAndDrawInterface {

    public Player(){
        Log.d("Player","Player instantiated");

        super.speed = 1;
        super.health = 100;
        super.xPosition = 100;
        super.yPosition = 180;
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
    public void draw(Canvas canvas, Paint paint) {

        //Log.d("Player","drawing player");

        paint.setColor(Color.BLUE);

        canvas.drawCircle(xPosition, yPosition, 15, paint);

        paint.setTextSize(40);
        canvas.drawText("Player", xPosition - 60, yPosition + 60, paint);
    }
}
