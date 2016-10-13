package com.gamelogic;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.core.UpdateAndDrawInterface;

public class Map implements UpdateAndDrawInterface{

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint, Context context) {

        //Log.d("Map","drawing map");

       // paint.setColor(Color.BLUE);

      //  canvas.drawCircle(xPosition, yPosition, 15, paint);

    }
}
