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

    protected BackgroundEntity(float baseHeight, float baseWidth, DisplayMetrics metrics) {
        this.metrics = metrics;
        baseRact = new RectF(-2000 + metrics.widthPixels / 2, -2000 + metrics.heightPixels / 2, 2000 + metrics.widthPixels / 2, 2000 + metrics.heightPixels / 2);
        outerBoarder = new RectF(metrics.widthPixels / 2, baseHeight - metrics.heightPixels / 2, baseWidth - metrics.widthPixels / 2, metrics.heightPixels / 2);
        ratio = (float) metrics.heightPixels / (float) metrics.widthPixels;
        int innerBoarderOffset = 150;
        innerBoarder = new RectF(innerBoarderOffset, metrics.heightPixels - innerBoarderOffset, metrics.widthPixels - innerBoarderOffset, innerBoarderOffset);
        this.uvs = new RectF(0f, 0f, 1f, 1f);
        mustDrawThis(true);
//        Log.d("BackgroundEntity", "baseHeight: " + baseHeight + " baseWidth: " + baseWidth + " baseRatio: " + ratio);
//        Log.d("BackgroundEntity", "BaseRect: " + rectToString(baseRact));
//        Log.d("BackgroundEntity", "uvs: " + rectToString(uvs));
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

    /**
     * Move the map
     *
     * @param direction
     * @param playerLock
     * @param player_tlbr players lock state
     */
    public void move(Direction direction, int playerLock, int player_tlbr) {
        Matrix transformationMatrix = new Matrix();
        directionLock.check(direction, outerBoarder, screenPos, player_tlbr);
        switch (~(playerLock | 0xFFFFFFFC)) {//Invert player lock
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
                break;
            default:
                Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
                break;
        }

        transformationMatrix.mapRect(baseRact);
    }


    public RectF getInnerBoarder() {
        return innerBoarder;
    }

    public RectF getOuterBoarder() {
        return outerBoarder;
    }
}
