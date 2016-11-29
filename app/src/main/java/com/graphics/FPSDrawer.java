package com.graphics;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;

/**
 * Created by PatrickKaalund on 1/11/2016.
 */

public class FPSDrawer {
    private SpriteEntityFactory fpsFactory;
    private Entity numberDrawer;
    private Entity numberDrawer2;

    public FPSDrawer(Context context){
        Log.d("FPSDrawer", "Creating FPSDrawer");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        fpsFactory = new SpriteEntityFactory(R.drawable.numbers_fps, 155, 120, 11, 1, new PointF(0, 0));

        Entity fpsDrawer = fpsFactory.createEntity();
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
//            Log.e("FPSDrawer", "Counter: " + counter);

            if(counter > 10){
//                numberDrawer2.mustDrawThis(true);
                numberDrawer.setCurrentSprite(counter / 10);
                numberDrawer2.setCurrentSprite(counter % 10);
            }else{
//                numberDrawer2.mustDrawThis(false);
                numberDrawer.setCurrentSprite(counter);
            }
        }else {
//            numberDrawer2.mustDrawThis(false);
            numberDrawer.setCurrentSprite(0);
        }
    }
}
