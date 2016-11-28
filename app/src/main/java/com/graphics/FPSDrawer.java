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
    private SpriteEntityFactory numberFactory;
    private Entity fpsDrawer;
    private Entity numberDrawer;
    private Entity numberDrawer2;

    public FPSDrawer(Context context){
        Log.d("FPSDrawer", "Creating FPSDrawer");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        fpsFactory = new SpriteEntityFactory(R.drawable.fps_small, 180, 180, 1, 1, new PointF(0, 0));
        numberFactory = new SpriteEntityFactory(R.drawable.numbers_red, 155, 120, 1, 10, new PointF(0, 0));

        fpsDrawer = fpsFactory.createEntity();
        fpsDrawer.placeAt(200, displayMetrics.heightPixels - 200);
        fpsDrawer.setCurrentSprite(0);

        numberDrawer = numberFactory.createEntity();
        numberDrawer.placeAt(290, displayMetrics.heightPixels - 200);
        numberDrawer.setCurrentSprite(0);

        numberDrawer2 = numberFactory.createEntity();
        numberDrawer2.placeAt(340, displayMetrics.heightPixels - 200);
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
