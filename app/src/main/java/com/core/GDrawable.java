package com.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by thor on 10/13/16.
 */

public abstract class GDrawable {
    public Context context;
    public ScreenDrawer screenDrawer;
//    public Paint paint;

    public GDrawable(Context context, ScreenDrawer screenDrawer){
        this.context = context;
        this.screenDrawer = screenDrawer;

//        paint.setStyle(Paint.Style.FILL);
    }


    public abstract void draw(Canvas canvas);
    public abstract void update();



}
