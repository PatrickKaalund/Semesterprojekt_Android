package com.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

public class ScreenDrawer extends View {

    //private Paint paint;
    private Context context;

    public ArrayList<GDrawable> objectsToDraw = new ArrayList<>();

    public ScreenDrawer(Context context) {
        super(context);

        this.context = context;

        //this.paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(GDrawable drawable : objectsToDraw){
            drawable.draw(canvas);
        }
    }
}

