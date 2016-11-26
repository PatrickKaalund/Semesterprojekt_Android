package com.graphics;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.gamelogic.DataContainer;
import com.gamelogic.DirectionLock;

import static com.gamelogic.DirectionLock.ALL_LOCKED;
import static com.gamelogic.DirectionLock.UNLOCKED;
import static com.gamelogic.DirectionLock.X_LOCKED;
import static com.gamelogic.DirectionLock.Y_LOCKED;
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
    public PointF screenPos;
    protected RectF uvs;
    float ratio;
    private DisplayMetrics metrics;
    public RectF innerBoarder;
    public RectF outerBoarder;
    private DirectionLock directionLock;
    private RectF baseRact;
    private RectF baseRactOposite;

    protected BackgroundEntity(float baseHeight, float baseWidth, DisplayMetrics metrics) {
        this.metrics = metrics;
//        this.baseHeight = baseHeight;
//        this.baseWidth = baseWidth;
        baseRact = new RectF(-2000 + metrics.widthPixels/2, - 2000 + metrics.heightPixels/2, 2000 + metrics.widthPixels/2, 2000 + metrics.heightPixels/2);
//        baseRact = new RectF(0, baseHeight, baseWidth, 0);
        outerBoarder = new RectF(metrics.widthPixels/2, baseHeight - metrics.heightPixels / 2, baseWidth - metrics.widthPixels/2, metrics.heightPixels / 2);
        // test
//        outerBoarder = new RectF(metrics.widthPixels/2 - 200, baseHeight - metrics.heightPixels / 2 + 200, baseWidth - metrics.widthPixels/2 + 200, metrics.heightPixels / 2 - 200);
        ratio = (float) metrics.heightPixels / (float) metrics.widthPixels;

        int innerBoarderOffset = 150;
        innerBoarder = new RectF(innerBoarderOffset, metrics.heightPixels - innerBoarderOffset, metrics.widthPixels - innerBoarderOffset, innerBoarderOffset);

        this.uvs = new RectF(0f, 0f, 1f, 1f);
//        this.uvs = new RectF(0f, 0f, 4f, 4f);
        mustDrawThis(true);
        Log.d("BackgroundEntity", "baseHeight: " + baseHeight + " baseWidth: " + baseWidth + " baseRatio: " + ratio);
        Log.d("BackgroundEntity", "BaseRect: " + rectToString(baseRact));
        Log.d("BackgroundEntity", "uvs: " + rectToString(uvs));
        screenPos = new PointF(2000, 2000);
        directionLock = new DirectionLock();
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

    public void move(Direction direction, int playerLock, int player_tlbr) {
        Matrix transformationMatrix = new Matrix();
        directionLock.check(direction, outerBoarder, screenPos, player_tlbr);
        switch (~(playerLock | 0xFFFFFFFC)) {
            case UNLOCKED:
                // Player is locked
                transformationMatrix.setTranslate(-direction.velocity_X, -direction.velocity_Y);
                screenPos.x += direction.velocity_X;
                screenPos.y += direction.velocity_Y;

                break;
            case X_LOCKED:
                // Player is locked in Y (inverted)
                transformationMatrix.setTranslate(0, -direction.velocity_Y);
                screenPos.y += direction.velocity_Y;

                break;
            case Y_LOCKED:
                // Player is locked in X (inverted)
                transformationMatrix.setTranslate(-direction.velocity_X, 0);
                screenPos.x += direction.velocity_X;

                break;
            case ALL_LOCKED:
                // Map is locked. Player is free
//                return;
                break;
            default:
                Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
                break;
        }

        transformationMatrix.mapRect(baseRact);
    }

//
//    public void moveFrame(float deltaX, float deltaY) {
//        Matrix transformationMatrix = new Matrix();
//        transformationMatrix.setTranslate(-deltaX, -deltaY);
//        transformationMatrix.mapRect(baseRact);
////
////        // Sanity checks :-)
////        // Corners
////
//////        LL(this, "Pos: " + DataContainer.player.getPos().x + ", " + DataContainer.player.getPos().y);
////
////        if(deltaX != 0 && deltaY != 0){
//////            Log.d("BackgroundEntity", "Condition Corners!");
////            // TL
////            if(deltaX < 0 && deltaY > 0){
////                if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0 && mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
////                    transformationMatrix.setTranslate(-deltaX, -deltaY);
////                    mapCenter.x += deltaX;
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().x += deltaX;
////                    DataContainer.player.getPos().y += deltaY;
////                }
////                else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2){
////                    transformationMatrix.setTranslate(0, -deltaY);
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().y += deltaY;
////                }else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX < 0){
////                    transformationMatrix.setTranslate(-deltaX, 0);
////                    mapCenter.x += deltaX;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = 0;
////                    DataContainer.player.getPos().x += deltaX;
////                }
////                else{
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = 0;
////                }
////            }
////            // TR
////            else if(deltaX > 0 && deltaY > 0){
////                if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0 && mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
////                    transformationMatrix.setTranslate(-deltaX, -deltaY);
////                    mapCenter.y += deltaY;
////                    mapCenter.x += deltaX;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().x += deltaX;
////                    DataContainer.player.getPos().y += deltaY;
////                }
////                else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2){
////                    transformationMatrix.setTranslate(0, -deltaY);
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().y += deltaY;
////                }else if(mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
////                    transformationMatrix.setTranslate(-deltaX, 0);
////                    mapCenter.x += deltaX;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = 0;
////                    DataContainer.player.getPos().x += deltaX;
////                }
////                else{
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = 0;
////                }
////            }
////            // BR
////            else if(deltaX > 0 && deltaY < 0){
////                if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0 && mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
////                    transformationMatrix.setTranslate(-deltaX, -deltaY);
////                    mapCenter.x += deltaX;
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().x += deltaX;
////                    DataContainer.player.getPos().y += deltaY;
////                }
////                else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0){
////                    transformationMatrix.setTranslate(0, -deltaY);
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().y += deltaY;
////                }else if(mapCenter.x + deltaX <= maxMapValue - metrics.widthPixels / 2){
////                    transformationMatrix.setTranslate(-deltaX, 0);
////                    mapCenter.x += deltaX;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = 0;
////                    DataContainer.player.getPos().x += deltaX;
////                }else{
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = 0;
////                }
////            }
////            // BL
////            else if(deltaX < 0 && deltaY < 0){
////                if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0 && mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
////                    transformationMatrix.setTranslate(-deltaX, -deltaY);
////                    mapCenter.x += deltaX;
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().x += deltaX;
////                    DataContainer.player.getPos().y += deltaY;
////                }
////                else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY <= 0){
////                    transformationMatrix.setTranslate(0, -deltaY);
////                    mapCenter.y += deltaY;
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = -deltaY;
////                    DataContainer.player.getPos().y += deltaY;
////                }else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX <= 0){
////                    transformationMatrix.setTranslate(-deltaX, 0);
////                    mapCenter.x += deltaX;
////                    DataContainer.mapMovement.x = -deltaX;
////                    DataContainer.mapMovement.y = 0;
////                    DataContainer.player.getPos().x += deltaX;
////                }else{
////                    DataContainer.mapMovement.x = 0;
////                    DataContainer.mapMovement.y = 0;
////                }
////            }
////        }
////        // top
////        else if(mapCenter.y - deltaY <= maxMapValue - metrics.heightPixels / 2 && deltaY > 0){
////            transformationMatrix.setTranslate(-deltaX, -deltaY);
////            mapCenter.y += deltaY;
////            DataContainer.mapMovement.x = 0;
////            DataContainer.mapMovement.y = -deltaY;
////            DataContainer.player.getPos().y += deltaY;
//////            Log.d("BackGroundEntity", "Condition 1. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
////        }
////        // bottom
////        else if(mapCenter.y - deltaY >= metrics.heightPixels / 2 + 100 && deltaY < 0){
////            transformationMatrix.setTranslate(-deltaX, -deltaY);
////            mapCenter.y += deltaY;
////            DataContainer.mapMovement.x = 0;
////            DataContainer.mapMovement.y = -deltaY;
////            DataContainer.player.getPos().y += deltaY;
//////            Log.d("BackGroundEntity", "Condition 2. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
////        }
////        // left
////        else if(mapCenter.x + deltaX >= metrics.widthPixels / 2 && deltaX < 0){
////            transformationMatrix.setTranslate(-deltaX, -deltaY);
////            mapCenter.x += deltaX;
////            DataContainer.mapMovement.x = -deltaX;
////            DataContainer.mapMovement.y = 0;
////            DataContainer.player.getPos().x += deltaX;
//////            Log.d("BackGroundEntity", "Condition 3. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
////        }
////        // right
////        else if(mapCenter.x + deltaX < maxMapValue - metrics.widthPixels / 2 && deltaX > 0){
////            transformationMatrix.setTranslate(-deltaX, 0);
////            mapCenter.x += deltaX;
////            DataContainer.mapMovement.x = -deltaX;
////            DataContainer.mapMovement.y = 0;
////            DataContainer.player.getPos().x += deltaX;
//////            Log.d("BackGroundEntity", "Condition 4. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
////        } else{
////            // No movement
////            DataContainer.mapMovement.x = 0;
////            DataContainer.mapMovement.y = 0;
//////            Log.d("BackGroundEntity", "Condition 5. Map center: " + mapCenter.x + ", " + mapCenter.y + ". Moving with: " + deltaX + ", " + deltaY + ". MapMovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);
////        }
////
//////        return
////
////
////
//////        entity.moveBy(-deltaX, -deltaY, 0);
////
//////        if (direction.lock == UNLOCK) {
//////            return direction;
//////        } else {
//////            transformationMatrix.setTranslate(direction.velocity_X, -direction.velocity_Y);
//////        }
////
////
////
////
//////        switch (direction.lock) {
//////            case Y:
//////                transformationMatrix.setTranslate(-direction.velocity_X, 0);
//////
//////                break;
//////            case X:
//////                transformationMatrix.setTranslate(0, -direction.velocity_Y);
//////
//////                break;
//////            case ALL:
//////                break;
//////            case UNLOCK:
//////                transformationMatrix.setTranslate(direction.velocity_X, -direction.velocity_Y);
//////                break;
//////        }
////
//////        Log.w("BackgroundEntity", "baseRact before: " + rectToString(baseRact));
////        transformationMatrix.mapRect(baseRact);
//////        Log.w("BackgroundEntity", "baseRact after: " + rectToString(baseRact));
//////        return direction;
//////        return null;
//    }

    public RectF getInnerBoarder() {
        return innerBoarder;
    }

    public RectF getOuterBoarder() {
        return outerBoarder;
    }
}
