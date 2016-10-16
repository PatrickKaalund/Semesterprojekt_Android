package com.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import com.example.patrickkaalund.semesterprojekt_android.R;

import java.util.ArrayList;

public class ScreenDrawer extends SurfaceView {

    Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int screenWidth;
    private int screenHeight;
    private Paint paint;

    public ArrayList<GDrawable> objectsToDraw = new ArrayList<>();

    public ScreenDrawer(Context context) {
        super(context);
        this.surfaceHolder = getHolder();

        // scaling - begin
        int scale = 1;

        screenWidth = (int) getResources().getDisplayMetrics().widthPixels / scale;
        screenHeight = (int) getResources().getDisplayMetrics().heightPixels / scale;

        Log.d("ScreenDrawer", "ScreenWitdh is: " + screenWidth);
        Log.d("ScreenDrawer", "ScreenHeight is: " + screenHeight);

        surfaceHolder.setFixedSize(screenWidth, screenHeight);
        // scaling - end

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(100);
        paint.setFakeBoldText(true);
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.surfaceHolder;
    }

    public void draw() {

        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();

            for (GDrawable drawable : objectsToDraw) {
                drawable.draw(canvas);
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public int getScreenWidth(){
        return this.screenWidth;
    }

    public int getScreenHeight(){
        return this.screenHeight;
    }

    public Paint getPaint(){
        return this.paint;
    }
}

