package com.core;

import android.content.Context;
import android.graphics.Canvas;

public class FPSDrawer extends GDrawable {
    private int fps;

    public FPSDrawer(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

        fps = 0;
    }

    @Override
    public void draw(Canvas canvas) {
        if(this.fps < 1000){
            canvas.drawText("FPS: " + this.fps, 50, 200, screenDrawer.getPaint());
        }else{
            canvas.drawText("FPS: " + 0, 50, 200, screenDrawer.getPaint());
        }
    }

    @Override
    public void update() {
        this.fps = game.getFPS();
    }
}
