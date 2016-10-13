package com.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GDrawable extends GUpdateable{
    public Context context;
    public ScreenDrawer screenDrawer;
//    public Paint paint;

    public GDrawable(Context context, ScreenDrawer screenDrawer){
        this.context = context;
        this.screenDrawer = screenDrawer;
//        paint.setStyle(Paint.Style.FILL);
    }

    public abstract void draw(Canvas canvas);
}
