package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;


public class ItemCommon extends Item{
    private Entity item;
    private Player player;
    private SpriteEntityFactory itemFactory;
    private boolean hasCollided = false;
    private ItemCommon.ItemList_e type;

    public enum ItemList_e {
        MEDIC,
        AMMO_YELLOW,
        AMMO_SHOTGUN_DEFAULT,
        AMMO_AK47_DEFAULT,
        AMMO_BLUE,
        AMMO_GUN_DEFAULT,
    }

    public ItemCommon(SpriteEntityFactory itemFactory, int size, ItemList_e item_type, PointF startLocation, Player player) {
        this.player = player;
        this.type = item_type;
        super.size = size;
        this.itemFactory = itemFactory;

        item = itemFactory.createEntity();
        item.setCurrentSprite(type.ordinal());

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

        // Check for collision with Player and pass item to Player
        if (!hasCollided && item.collision(player.getPlayerEntity().getPosition())) {
            hasCollided = true;
            handlePickup();
            item.delete();
        }
    }

    private void handlePickup() {
        Log.d("ItemCommon", type.toString() + " SIZE: " + size + " GOT PICKED UP!");
        player.registerPickup(this);
    }

    public RectF getRect(){ return this.item.getRect(); }

    public ItemList_e getType() { return this.type; }
}
