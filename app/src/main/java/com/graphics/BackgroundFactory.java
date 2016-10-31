package com.graphics;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

/**
 * Created by thor on 10/30/16.
 */

public class BackgroundFactory extends EntityFactory{
    private Context context;
    DisplayMetrics metrics;

    public BackgroundFactory(int bmpId, Context context) {
        super(bmpId, 1, 1);
        this.context = context;
        this.metrics = context.getResources().getDisplayMetrics();

        //If index 0 is a background replace it else
        // make this index 0 so we draw the background first
        if(GlRendere.drawList.isEmpty()){
            GlRendere.drawList.add(this);
        }else if(GlRendere.drawList.get(0) instanceof BackgroundFactory){
            GlRendere.drawList.set(0, this);
        }else{
            GlRendere.drawList.add(0, this);
        }

    }

    public BackgroundEntity crateEntity() {
        entityDrawCount++;
        BackgroundEntity newBackground = new BackgroundEntity(
                metrics.heightPixels,
                metrics.widthPixels
        );
        productionLine.add(newBackground);
        return newBackground;
    }

}
