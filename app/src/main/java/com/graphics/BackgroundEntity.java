package com.graphics;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.gamelogic.DataContainer;

import static com.graphics.Direction.ALL;
import static com.graphics.Direction.UNLOCK;
import static com.graphics.Direction.X;
import static com.graphics.Direction.Y;
import static com.graphics.GraphicsTools.LL;
import static com.graphics.GraphicsTools.getCornersFromRect;
import static com.graphics.GraphicsTools.getCornersFromRectWithZ;
import static com.graphics.GraphicsTools.rectToString;

/**
 * Created by thor on 10/30/16.
 */

public class BackgroundEntity extends GraphicEntity {
    protected RectF uvs;
    private float baseHeight;
    private float baseWidth;
    float ratio;
    private PointF mapCenter;
    private float maxMapValue = 4000;
    private DisplayMetrics metrics;

    protected BackgroundEntity(float baseHeight, float baseWidth, DisplayMetrics metrics) {
        this.metrics = metrics;
        this.baseHeight = baseHeight;
        this.baseWidth = baseWidth;
//        baseRact = new RectF(-baseWidth/2, -baseHeight/2, baseWidth/2, baseHeight/2);
        baseRact = new RectF(0,0, baseWidth, baseHeight);
        ratio = (float) metrics.heightPixels / (float) metrics.widthPixels;

        this.uvs = new RectF(0f, 0f, 4f, 4f);
        mustDrawThis(true);
        Log.d("BackgroundEntity", "baseHeight: " + baseHeight + " baseWidth: " + baseWidth + " baseRatio: " + ratio);
        Log.d("BackgroundEntity", "BaseRect: " + rectToString(baseRact));
        Log.d("BackgroundEntity", "uvs: " + rectToString(uvs));

        mapCenter = new PointF(metrics.widthPixels/2, metrics.heightPixels/2);
    }

    @Override
    protected float[] getUvs() {


        return getCornersFromRect(uvs);
    }

    @Override
    protected float[] getModel() {
        return getCornersFromRectWithZ(baseRact);
    }

    @Override
    public void mustDrawThis(boolean draw) {
        drawThis = draw;
    }

    @Override
    public boolean mustDrawThis() {
        return drawThis;
    }

//    @Override
//    public void setLock(LockDirection lockDirection) {
//        this.lockDirection = lockDirection;
//    }

    public void moveFrame(float deltaX, float deltaY) {
        Matrix transformationMatrix = new Matrix();

        // Sanity checks :-)
        // Corners

//        LL(this, "Pos: " + DataContainer.player.getPos().x + ", " + DataContainer.player.getPos().y);

        if(deltaX != 0 && deltaY != 0){
//            Log.d("BackgroundEntity", "Condition Corners!");
            // TL
            if(deltaX < 0 && deltaY > 0){
                if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0 && mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
                    transformationMatrix.setTranslate(-deltaX, -deltaY);
                    mapCenter.x += deltaX;
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().x += deltaX;
                    DataContainer.player.getPos().y += deltaY;
                }
                else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2){
                    transformationMatrix.setTranslate(0, -deltaY);
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().y += deltaY;
                }else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX < 0){
                    transformationMatrix.setTranslate(-deltaX, 0);
                    mapCenter.x += deltaX;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = 0;
                    DataContainer.player.getPos().x += deltaX;
                }
                else{
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = 0;
                }
            }
            // TR
            else if(deltaX > 0 && deltaY > 0){
                if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0 && mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
                    transformationMatrix.setTranslate(-deltaX, -deltaY);
                    mapCenter.y += deltaY;
                    mapCenter.x += deltaX;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().x += deltaX;
                    DataContainer.player.getPos().y += deltaY;
                }
                else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2){
                    transformationMatrix.setTranslate(0, -deltaY);
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().y += deltaY;
                }else if(mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
                    transformationMatrix.setTranslate(-deltaX, 0);
                    mapCenter.x += deltaX;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = 0;
                    DataContainer.player.getPos().x += deltaX;
                }
                else{
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = 0;
                }
            }
            // BR
            else if(deltaX > 0 && deltaY < 0){
                if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0 && mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
                    transformationMatrix.setTranslate(-deltaX, -deltaY);
                    mapCenter.x += deltaX;
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().x += deltaX;
                    DataContainer.player.getPos().y += deltaY;
                }
                else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0){
                    transformationMatrix.setTranslate(0, -deltaY);
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().y += deltaY;
                }else if(mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
                    transformationMatrix.setTranslate(-deltaX, 0);
                    mapCenter.x += deltaX;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = 0;
                    DataContainer.player.getPos().x += deltaX;
                }else{
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = 0;
                }
            }
            // BL
            else if(deltaX < 0 && deltaY < 0){
                if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0 && mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
                    transformationMatrix.setTranslate(-deltaX, -deltaY);
                    mapCenter.x += deltaX;
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().x += deltaX;
                    DataContainer.player.getPos().y += deltaY;
                }
                else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0){
                    transformationMatrix.setTranslate(0, -deltaY);
                    mapCenter.y += deltaY;
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = -deltaY;
                    DataContainer.player.getPos().y += deltaY;
                }else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
                    transformationMatrix.setTranslate(-deltaX, 0);
                    mapCenter.x += deltaX;
                    DataContainer.mapMovement.x = -deltaX;
                    DataContainer.mapMovement.y = 0;
                    DataContainer.player.getPos().x += deltaX;
                }else{
                    DataContainer.mapMovement.x = 0;
                    DataContainer.mapMovement.y = 0;
                }
            }
        }
        // top
        else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0){
            transformationMatrix.setTranslate(-deltaX, -deltaY);
            mapCenter.y += deltaY;
            DataContainer.mapMovement.x = 0;
            DataContainer.mapMovement.y = -deltaY;
            DataContainer.player.getPos().y += deltaY;
//            Log.d("BackGroundEntity", "Condition 1. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
        }
        // bottom
        else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY < 0){
            transformationMatrix.setTranslate(-deltaX, -deltaY);
            mapCenter.y += deltaY;
            DataContainer.mapMovement.x = 0;
            DataContainer.mapMovement.y = -deltaY;
            DataContainer.player.getPos().y += deltaY;
//            Log.d("BackGroundEntity", "Condition 2. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
        }
        // left
        else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX < 0){
            transformationMatrix.setTranslate(-deltaX, -deltaY);
            mapCenter.x += deltaX;
            DataContainer.mapMovement.x = -deltaX;
            DataContainer.mapMovement.y = 0;
            DataContainer.player.getPos().x += deltaX;
//            Log.d("BackGroundEntity", "Condition 3. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
        }
        // right
        else if(mapCenter.x + deltaX < maxMapValue - metrics.widthPixels / 2 && deltaX > 0){
            transformationMatrix.setTranslate(-deltaX, 0);
            mapCenter.x += deltaX;
            DataContainer.mapMovement.x = -deltaX;
            DataContainer.mapMovement.y = 0;
            DataContainer.player.getPos().x += deltaX;
//            Log.d("BackGroundEntity", "Condition 4. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
        } else{
            // No movement
            DataContainer.mapMovement.x = 0;
            DataContainer.mapMovement.y = 0;
//            Log.d("BackGroundEntity", "Condition 5. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
        }

//        return



//        entity.moveBy(-deltaX, -deltaY, 0);

//        if (direction.lock == UNLOCK) {
//            return direction;
//        } else {
//            transformationMatrix.setTranslate(direction.velocity_X, -direction.velocity_Y);
//        }




//        switch (direction.lock) {
//            case Y:
//                transformationMatrix.setTranslate(-direction.velocity_X, 0);
//
//                break;
//            case X:
//                transformationMatrix.setTranslate(0, -direction.velocity_Y);
//
//                break;
//            case ALL:
//                break;
//            case UNLOCK:
//                transformationMatrix.setTranslate(direction.velocity_X, -direction.velocity_Y);
//                break;
//        }

//        Log.w("BackgroundEntity", "baseRact before: " + rectToString(baseRact));
        transformationMatrix.mapRect(baseRact);
//        Log.w("BackgroundEntity", "baseRact after: " + rectToString(baseRact));
//        return direction;
//        return null;
    }

}
