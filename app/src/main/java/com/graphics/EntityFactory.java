package com.graphics;

import java.util.ArrayList;

import static com.graphics.GlRenderer.drawList;

abstract class EntityFactory {
    ArrayList<GraphicEntity> productionLine = new ArrayList<>();
    int textureID;
    int textureAtlasRows;
    int textureAtlasColumns;
    int entityDrawCount;
    int index;
    int bitMapID;
    boolean textureLoaded;
    EntityFactory(int bmpId, int textureAtlasRows, int textureAtlasColumns){
        this.bitMapID = bmpId;
        this.index = drawList.size();
        this.textureAtlasRows = textureAtlasRows;
        this.textureAtlasColumns = textureAtlasColumns;
        this.entityDrawCount = 0;
        textureLoaded = false;
    }

}
