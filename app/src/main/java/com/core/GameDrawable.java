package com.core;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class GameDrawable extends View {

    float rotation = -45;

    public GameDrawable(Context context) {
        super(context);
    }


    @Override
    protected void onDraw(Canvas c) {
        Paint paint = new Paint(); // burde ske udenfor onDraw() for bedre ydelse
        paint.setStyle(Paint.Style.FILL);

        // lav baggrunden hvid
        paint.setColor(Color.WHITE);
        c.drawPaint(paint);

        // tegn noget roteret tekst
        paint.setPathEffect(null);
        paint.setColor(Color.argb(128, 0, 255, 0)); // halvgennemsigtig grøn
        paint.setTextSize(48);
        String roteretTekst = "Tryk for at rotere";

        // find tekstens størrelse på skærmen
        Rect tekstomrids = new Rect();
        paint.getTextBounds(roteretTekst, 0, roteretTekst.length(), tekstomrids);

        int x = getWidth() / 2;
        int y = getHeight() / 2;
        // rotér lærredet omkring tekstens center
        c.rotate(rotation, x , y );
        // tegn teksten
        c.drawText(roteretTekst, x, y, paint);

        // nulstil rotation (og translation etc)
        c.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        rotation = event.getX();
        invalidate(); // Får Android til at kalde onDraw() på viewet
        return true;
    }
}

