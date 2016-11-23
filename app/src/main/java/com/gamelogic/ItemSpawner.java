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
    private DisplayMetrics displayMetrics;

    public ItemSpawner(Context c){
        this.displayMetrics = c.getResources().getDisplayMetrics();
        PointF sizeOfItem = new PointF(100, 100);
        itemFactory = new SpriteEntityFactory(R.drawable.medic, sizeOfItem.x, sizeOfItem.y, 1, 1, new PointF(0, 0));
        items = new ArrayList<>();
    }

    public void spawn(int size, int type, PointF startLocation){
        ItemCommon item = new ItemCommon(itemFactory, size, type, startLocation);
        items.add(item);
    }

    public void spawnRandom(){
        Random rand = new Random();

        int randType = rand.nextInt(4);
        int randSize = rand.nextInt(4);

        float minX, minY, maxX, maxY;

        minX = 150;
        maxX = displayMetrics.widthPixels - 150;
        minY = 150;
        maxY = displayMetrics.heightPixels - 150;
        float randomX = rand.nextFloat() * (maxX - minX) + minX;
        float randomY = rand.nextFloat() * (maxY - minY) + minY;

        Log.d("ItemSpawner", "Spawn place: X=" + randomX + " Y=" + randomY + " Type: " + randType + " Size: " + randSize);
        ItemCommon item = new ItemCommon(itemFactory, randSize, randType, new PointF(randomX, randomY));
        items.add(item);
    }

    public void spawnItems(int size, int type, int numberOfItems, PointF startLocation){
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
