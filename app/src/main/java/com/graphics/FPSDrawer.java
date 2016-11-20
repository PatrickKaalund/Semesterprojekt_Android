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
    private Entity fpsDrawer;

    public FPSDrawer(Context context){
        Log.d("FPSDrawer", "Creating FPSDrawer");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        fpsFactory = new SpriteEntityFactory(R.drawable.fps, 70, 200, 73, 1, new PointF(0, 0));

        fpsDrawer = fpsFactory.createEntity();
        fpsDrawer.placeAt(200, displayMetrics.heightPixels - 200);
//        fpsDrawer.setCurrentSprite(72);
    }

    public void update(int counter){
        if(counter < 72){
            fpsDrawer.setCurrentSprite(counter);
        }else {
            fpsDrawer.setCurrentSprite(72); // not available
        }
    }
}
