package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 23/11/2016.
 */

public class MapBorder {

    private DisplayMetrics displayMetrics;
    private PointF sizeOfBorderEntity;
    private SpriteEntityFactory mapBorderFactory;
    private ArrayList<Entity> borderEntities;

    public MapBorder(Context context){
        this.displayMetrics = context.getResources().getDisplayMetrics();
        sizeOfBorderEntity = new PointF(450, 450);
        mapBorderFactory = new SpriteEntityFactory(R.drawable.tree2, sizeOfBorderEntity.x, sizeOfBorderEntity.y, 1, 1, new PointF(0, 0));
        borderEntities = new ArrayList<>();

        initialiseBorder();
    }

    //          Tree border
    private void initialiseBorder() {
//        float offset = 0;
//        for(int index = 0; index < metrics.heightPixels; index += 250) {
//            Entity borderEntityLeft = borderFactory.createEntity();
//            Entity borderEntityRight = borderFactory.createEntity();
//            Entity borderEntityTop = borderFactory.createEntity();
//            Entity borderEntityBottom = borderFactory.createEntity();
//            borderEntityLeft.placeAt(offset, index);
//            borderEntityRight.placeAt(metrics.widthPixels - offset, index);
//            borderEntityTop.placeAt(index, metrics.heightPixels - 40);
//            borderEntityBottom.placeAt(index, offset);
//        }
        float offset = 0;
        for(int index = 0; index < DataContainer.mapGlobalSize.y; index += 250) {
            Entity borderEntityTop = mapBorderFactory.createEntity();
            Entity borderEntityBottom = mapBorderFactory.createEntity();
            borderEntityTop.placeAt(index, DataContainer.mapGlobalSize.y - 40);
            borderEntityBottom.placeAt(index, offset);
            borderEntities.add(borderEntityTop);
            borderEntities.add(borderEntityBottom);
        }
        for(int index = 0; index < DataContainer.mapGlobalSize.x; index += 250) {
            Entity borderEntityLeft = mapBorderFactory.createEntity();
            Entity borderEntityRight = mapBorderFactory.createEntity();
            borderEntityLeft.placeAt(offset, index);
            borderEntityRight.placeAt(DataContainer.mapGlobalSize.x - offset, index);
            borderEntities.add(borderEntityLeft);
            borderEntities.add(borderEntityRight);
        }
    }

    public void update() {
        // Adjust for map
        for(Entity entity : borderEntities){
            entity.moveBy(DataContainer.mapMovement.x, DataContainer.mapMovement.y, 0);
        }
    }

//          Zombie border
//    private void initialiseBorder() {
//        float offset = 40;
//        for(int index = 0; index < metrics.heightPixels; index += 200) {
//            Entity borderEntityLeft = borderFactory.createEntity();
//            Entity borderEntityRight = borderFactory.createEntity();
//            Entity borderEntityTop = borderFactory.createEntity();
//            Entity borderEntityBottom = borderFactory.createEntity();
//            borderEntityLeft.setCurrentSprite(4);
//            borderEntityRight.setCurrentSprite(4);
//            borderEntityTop.setCurrentSprite(4);
//            borderEntityBottom.setCurrentSprite(4);
//            borderEntityLeft.placeAt(offset, index);
//            borderEntityRight.placeAt(metrics.widthPixels - offset, index);
//            borderEntityTop.placeAt(index, metrics.heightPixels - 80);
//            borderEntityBottom.placeAt(index, offset);
//        }
//    }
}
