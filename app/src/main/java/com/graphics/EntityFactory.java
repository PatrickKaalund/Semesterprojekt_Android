package com.graphics;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import static com.graphics.GlRendere.drawList;

/**
 * Created by thor on 10/27/16.
 */

public class EntityFactory {
    private final int textureAtlasRows;
    private final int textureAtlasColumns;
    protected int textureID;
    protected int textureName;
    protected int spriteCount;

    protected ArrayList<RectF> sprites = new ArrayList<>();
    protected ArrayList<GraphicInternEntity> productionLine = new ArrayList<>();
    private float modelBaseHeight;
    private float modelBaseWidth;
    private PointF pos;
    protected int entityDrawCount = 0;
    private int index;
    public int bitMapID;
    protected boolean textureLoaded;

    public EntityFactory(int bmpId, float modelBaseHeight, float modelBaseWidth,
                         int textureAtlasRows, int textureAtlasColumns,
                         PointF pos) {
        this.modelBaseHeight = modelBaseHeight;
        this.modelBaseWidth = modelBaseWidth;
        this.pos = pos;
        this.bitMapID = bmpId;
        this.textureAtlasRows = textureAtlasRows;
        this.textureAtlasColumns = textureAtlasColumns;
        this.index = drawList.size();
        makeSprites();
        textureLoaded = false;
        GlRendere.drawList.add(this);
        GlRendere.durtyDrawList = false;
    }

    public Entity crateEntity() {
        PointF newPoint = new PointF();
        newPoint.set(pos);

        GraphicInternEntity newEntity = new GraphicInternEntity(
                modelBaseHeight,
                modelBaseWidth,
                newPoint,
                this,
                productionLine.size());

        productionLine.add(newEntity);
        return newEntity;
    }

    public void removeEntity(GraphicInternEntity e) {
        if (e.mustDrawThis()) {
            entityDrawCount--;
        }
        productionLine.remove(e.index);
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



}
