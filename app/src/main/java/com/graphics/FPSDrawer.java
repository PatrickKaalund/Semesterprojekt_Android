package com.graphics;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;


class FPSDrawer {
    private Entity numberDrawer;
    private Entity numberDrawer2;
    private Entity fpsDrawer;
    SpriteEntityFactory fpsFactory;

    FPSDrawer(Context context){
        Log.d("FPSDrawer", "Creating FPSDrawer");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        fpsFactory = new SpriteEntityFactory(R.drawable.numbers_fps, 155, 120, 11, 1, new PointF(0, 0));

        fpsDrawer = fpsFactory.createEntity();
        fpsDrawer.placeAt(125, displayMetrics.heightPixels - 180);
        fpsDrawer.setCurrentSprite(10);

        numberDrawer = fpsFactory.createEntity();
        numberDrawer.placeAt(240, displayMetrics.heightPixels - 180);
        numberDrawer.setCurrentSprite(0);

        numberDrawer2 = fpsFactory.createEntity();
        numberDrawer2.placeAt(290, displayMetrics.heightPixels - 180);
        numberDrawer2.setCurrentSprite(0);
    }

    public void update(int counter){
        if(counter < 100){

            if(counter > 9){
                numberDrawer.setCurrentSprite(counter / 10);
                numberDrawer2.setCurrentSprite(counter % 10);
            }else{
                numberDrawer.setCurrentSprite(counter);
            }
        }else {
            numberDrawer.setCurrentSprite(0);
        }
    }
}
