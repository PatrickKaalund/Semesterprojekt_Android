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
        item.setCurrentSprite(type);

        placeElementFromGlobalPos(startLocation);

        item.setAngleOffSet(180);
    }

    private void placeElementFromGlobalPos(PointF globalPos){
        PointF initialPosOnScreen = new PointF();
        initialPosOnScreen.x = globalPos.x - DataContainer.player.getPos().x + DataContainer.gameContext.getResources().getDisplayMetrics().widthPixels / 2;
        initialPosOnScreen.y = globalPos.y - DataContainer.player.getPos().y + DataContainer.gameContext.getResources().getDisplayMetrics().heightPixels / 2;
        this.item.placeAt(initialPosOnScreen.x, initialPosOnScreen.y);
        this.item.setPosition(new PointF(globalPos.x, globalPos.y));
    }


    public void update() {

        // Adjust for map movement
        item.moveBy(-DataContainer.mapMovement.x, -DataContainer.mapMovement.y);

    }

    public RectF getRect(){ return this.item.getRect(); }
}
