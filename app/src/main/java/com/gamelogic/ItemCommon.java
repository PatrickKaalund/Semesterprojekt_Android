package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;

import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

public class ItemCommon extends Item{
    private Entity item;

    public ItemCommon(SpriteEntityFactory itemFactory, int size, int type, PointF startLocation) {

        super.size = size;
        super.type = type;
        item = itemFactory.createEntity();
        item.setCurrentSprite(0);
        item.placeAt(startLocation.x, startLocation.y);
        item.setAngleOffSet(180);
    }


    public void update() {

        // Adjust for map movement
        item.moveBy(DataContainer.mapMovement.x, DataContainer.mapMovement.y);

        item.drawNextSprite();
    }

    public RectF getRect(){ return this.item.getRect(); }
}
