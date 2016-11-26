package com.graphics;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import static com.graphics.GlRendere.drawList;
import static com.graphics.GraphicsTools.LL;

/**
 * Created by thor on 10/27/16.
 */

public class SpriteEntityFactory extends EntityFactory {

    protected int spriteCount;
    protected ArrayList<RectF> sprites = new ArrayList<>();
    private float modelBaseHeight;
    private float modelBaseWidth;
    private PointF pos;

    public SpriteEntityFactory(int bmpId, float modelBaseHeight, float modelBaseWidth,
                               int textureAtlasRows, int textureAtlasColumns,
                               PointF pos) {
        super(bmpId, textureAtlasRows, textureAtlasColumns);
        this.modelBaseHeight = modelBaseHeight;
        this.modelBaseWidth = modelBaseWidth;
        this.pos = pos;
        makeSprites();
        GlRendere.drawList.add(this);
    }

    public Entity createEntity() {
        PointF newPoint = new PointF();
        newPoint.set(pos);

        SpriteEntity newEntity = new SpriteEntity(
                modelBaseHeight,
                modelBaseWidth,
                newPoint,
                this
//                productionLine.size()
        );
        newEntity.index = productionLine.size();
        productionLine.add(newEntity);
        return newEntity;
    }


    public void removeEntity(Entity e) {
        SpriteEntity se = (SpriteEntity) e;
        if (se.mustDrawThis()) {
            entityDrawCount--;
        }
//        LL(this, "Removing idex: " + se.index + " list has size: " + productionLine.size());
        productionLine.remove(se);
    }

    public void delete() {
        drawList.remove(this.index);
    }

    private void makeSprites() {
        spriteCount = textureAtlasColumns * textureAtlasRows;
        float xOffset = 1 / (float) textureAtlasColumns;
        float yOffset = 1 / (float) textureAtlasRows;
        RectF subTexture = new RectF();
        subTexture.right = xOffset;
        subTexture.bottom = yOffset;
        for (int i = 0; i < textureAtlasRows; i++) {
            for (int j = 0; j < textureAtlasColumns; j++) {
                sprites.add(new RectF(subTexture));
                subTexture.left = subTexture.right;
                subTexture.right += xOffset;
            }
            subTexture.top = subTexture.bottom;
            subTexture.bottom += yOffset;
            subTexture.left = 0;
            subTexture.right = xOffset;
        }
    }

    public String spritesToString() {
        return GraphicsTools.allVertecisToString(sprites);
    }

    public PointF getPos() {
        return this.pos;
    }

}
