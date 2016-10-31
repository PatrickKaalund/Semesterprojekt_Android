package com.graphics;

import java.util.ArrayList;

import static com.graphics.GlRendere.drawList;

/**
 * Created by thor on 10/31/16.
 */

abstract class EntityFactory {
    protected ArrayList<GraphicEntity> productionLine = new ArrayList<>();
    protected int textureID;
    protected int textureAtlasRows;
    protected int textureAtlasColumns;
    protected int entityDrawCount;
    protected int index;
    public int bitMapID;
    protected boolean textureLoaded;
    protected EntityFactory(int bmpId,  int textureAtlasRows, int textureAtlasColumns){
        this.bitMapID = bmpId;
        this.index = drawList.size();
        this.textureAtlasRows = textureAtlasRows;
        this.textureAtlasColumns = textureAtlasColumns;
        this.entityDrawCount = 0;
        textureLoaded = false;
    }

}
