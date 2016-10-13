package com.gamelogic;


import android.content.Context;
import android.graphics.Canvas;

import com.core.GDrawable;
import com.core.ScreenDrawer;

public class Map extends GDrawable {



    public Map(Context context, ScreenDrawer screenDrawer) {
        super(context, screenDrawer);
        game.objectsToUpdate.add(this);
        screenDrawer.objectsToDraw.add(this);

    }


    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }

//    @Override
//    public void update() {
//
//    }
//
//    @Override
//    public void draw(Canvas canvas, Paint paint, Context context) {

        //Log.d("Map","drawing map");

       // paint.setColor(Color.BLUE);

      //  canvas.drawCircle(xPosition, yPosition, 15, paint);

//    }
}
