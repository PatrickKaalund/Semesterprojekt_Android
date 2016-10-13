package com.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface UpdateAndDrawInterface {
    public abstract void update();
    public abstract void draw(Canvas canvas, Paint paint, Context context);
}
