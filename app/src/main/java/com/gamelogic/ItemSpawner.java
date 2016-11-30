package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Random;

public class ItemSpawner {

    private SpriteEntityFactory itemFactory;
    private ArrayList<ItemCommon> items;
    private Player player;

    public ItemSpawner(Context c){
        DisplayMetrics displayMetrics = c.getResources().getDisplayMetrics();
        PointF sizeOfItem = new PointF(100, 100);
        itemFactory = new SpriteEntityFactory(R.drawable.drops_scaled, sizeOfItem.x, sizeOfItem.y, 6, 1, new PointF(0, 0));

        items = new ArrayList<>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void spawn(int size, ItemCommon.ItemList_e type, PointF startLocation){
        ItemCommon item = new ItemCommon(itemFactory, size, type, startLocation, player);
        items.add(item);
    }

    public void spawnRandom(){
        Random rand = new Random();

        int randType = rand.nextInt(6);
        int randSize = rand.nextInt(100);

        float minX, minY, maxX, maxY;

        minX = 150;
        maxX = DataContainer.mapGlobalSize.x - 150;
        minY = 150;
        maxY = DataContainer.mapGlobalSize.y - 150;
        float randomX = rand.nextFloat() * (maxX - minX) + minX;
        float randomY = rand.nextFloat() * (maxY - minY) + minY;

        Log.d("ItemSpawner", "Spawn place: X=" + randomX + " Y=" + randomY + " Type: " + randType + " Size: " + randSize);

        ItemCommon.ItemList_e itemList_e = ItemCommon.ItemList_e.MEDIC;

        switch (randType) {
            case 0 :
                itemList_e = ItemCommon.ItemList_e.MEDIC;
                break;
            case 1 :
                itemList_e = ItemCommon.ItemList_e.AMMO_YELLOW;
                break;
            case 2 :
                itemList_e = ItemCommon.ItemList_e.AMMO_SHOTGUN_DEFAULT;
                break;
            case 3 :
                itemList_e = ItemCommon.ItemList_e.AMMO_AK47_DEFAULT;
                break;
            case 4 :
                itemList_e = ItemCommon.ItemList_e.AMMO_BLUE;
                break;
            case 5 :
                itemList_e = ItemCommon.ItemList_e.AMMO_GUN_DEFAULT;
                break;
        }

        ItemCommon item = new ItemCommon(itemFactory, randSize, itemList_e, new PointF(randomX, randomY), player);
        items.add(item);
    }

    public void spawnItems(int size, ItemCommon.ItemList_e type, int numberOfItems, PointF startLocation){
        for(int counter = 0; counter < numberOfItems; counter++){
            spawn(size, type, startLocation);
        }
    }

    public void spawnItemsRandom(int numberOfItems){
        for(int counter = 0; counter < numberOfItems; counter++){
            spawnRandom();
        }
    }

    public void update(){
        for(ItemCommon e: items){
            e.update();
        }
    }
}
