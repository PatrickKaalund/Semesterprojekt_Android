package com.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class GameDrawable extends View {

    private GameStateHandler gameStateHandler;
    private Paint paint;

    public GameDrawable(Context context, GameStateHandler gameStateHandler) {
        super(context);

        this.gameStateHandler = gameStateHandler;
        this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);

        // set background color
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);

        //Log.d("GameDrawable","onDraw()");

        gameStateHandler.draw(canvas, paint);
    }
}

