package com.graphics;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import com.gamelogic.DataContainer;

/**
 * Created by thor on 10/30/16.
 */

public class BackgroundFactory extends EntityFactory {
    DisplayMetrics metrics;

    public BackgroundFactory(int bmpId, DisplayMetrics metrics) {
        super(bmpId, 1, 1);
        this.metrics = metrics;
        index = 0;

        /**
         *   If index 0 is a background replace it else
         *    make this index 0 so we draw the background first
         */
        if (GlRendere.drawList.isEmpty()) {
            GlRendere.drawList.add(this);
        } else if (GlRendere.drawList.get(0) instanceof BackgroundFactory) {
            GlRendere.drawList.set(0, this);
        } else {
            GlRendere.drawList.add(0, this);
        }

    }

    public BackgroundEntity createEntity(float sizeX, float sizeY) {
        entityDrawCount++;
        DataContainer.mapGlobalSize.x = sizeX;//Save the size for other to use
        DataContainer.mapGlobalSize.y = sizeY;
        BackgroundEntity newBackground = new BackgroundEntity(
                DataContainer.mapGlobalSize.x,
                DataContainer.mapGlobalSize.y,
                metrics
        );
        DataContainer.mapBaseRect = newBackground.getOuterBoarder();
        productionLine.add(newBackground);
        return newBackground;
    }


}
