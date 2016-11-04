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

    public BackgroundFactory(int bmpId, DisplayMetrics metrics) {
        super(bmpId, 1, 1);
        this.metrics = metrics;
        index = 0;
        this.context = context;
//        metrics.

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


//        protected void makeSprites() {
//        spriteCount = textureAtlasColumns * textureAtlasRows;
//        float xOffset = 1 / (float) textureAtlasColumns;
//        float yOffset = 1 / (float) textureAtlasRows;
//        RectF subTexture = new RectF();
//        subTexture.right = xOffset;
//        subTexture.bottom = yOffset;
//        for (int i = 0; i < textureAtlasRows; i++) {
//            for (int j = 0; j < textureAtlasColumns; j++) {
//                sprites.add(new RectF(subTexture));
//                subTexture.left = subTexture.right;
//                subTexture.right += xOffset;
//            }
//            subTexture.top = subTexture.bottom;
//            subTexture.bottom += yOffset;
//            subTexture.left = 0;
//            subTexture.right = xOffset;
//        }

}
