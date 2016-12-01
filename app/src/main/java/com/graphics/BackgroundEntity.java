package com.graphics;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.gamelogic.DataContainer;
import com.gamelogic.DirectionLock;
import com.gamelogic.Player;

import static com.gamelogic.DirectionLock.ALL_LOCKED;
import static com.gamelogic.DirectionLock.UNLOCKED;
import static com.gamelogic.DirectionLock.X_LOCKED;
import static com.gamelogic.DirectionLock.Y_LOCKED;
import static com.graphics.GraphicsTools.getCornersFromRect;
import static com.graphics.GraphicsTools.getCornersFromRectWithZ;

/**
 * Created by thor on 10/30/16.
 */

public class BackgroundEntity extends GraphicEntity {
    public PointF screenPos;
    protected RectF uvs;
    private DisplayMetrics metrics;
    public RectF innerBoarder;
    public RectF outerBoarder;
    private DirectionLock directionLock;
    private RectF baseRact;

    protected BackgroundEntity(float baseHeight, float baseWidth, PointF screenGlobalPos, DisplayMetrics metrics, int innerBoarderOffset) {
        this.metrics = metrics;
        screenPos = screenGlobalPos;
        directionLock = new DirectionLock();
        mustDrawThis(true);


        baseRact = new RectF( //Background model on screen
                -2000 + metrics.widthPixels / 2,
                -2000 + metrics.heightPixels / 2,
                2000 + metrics.widthPixels / 2,
                2000 + metrics.heightPixels / 2);

        outerBoarder = new RectF(//Map confinement boarder
                metrics.widthPixels / 2,
                baseHeight - metrics.heightPixels / 2,
                baseWidth - metrics.widthPixels / 2,
                metrics.heightPixels / 2);

        innerBoarder = new RectF(//Screen confinement boarder
                innerBoarderOffset,
                metrics.heightPixels - innerBoarderOffset,
                metrics.widthPixels - innerBoarderOffset,
                innerBoarderOffset);

        this.uvs = new RectF(0f, 0f, 4f, 4f);// Texture drawing coord

//        Log.d("BackgroundEntity", "baseHeight: " + baseHeight + " baseWidth: " + baseWidth);
//        Log.d("BackgroundEntity", "BaseRect: " + baseRact.toString());
//        Log.d("BackgroundEntity", "uvs: " + rectToString(uvs));

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
     * @param player
     */
    public void move(Direction direction, Player player) {
        Matrix transformationMatrix = new Matrix();
        directionLock.check(direction, outerBoarder, screenPos, player.playerTLBR);
        DataContainer.instance.mapMovement.x = 0;
        DataContainer.instance.mapMovement.y = 0;
        switch (~(player.playerLock | 0xFFFFFFFC)) {//Invert player lock
            case UNLOCKED:
                // Player is locked
                transformationMatrix.setTranslate(-direction.velocity_X, -direction.velocity_Y);
                DataContainer.instance.mapMovement.x = direction.velocity_X;
                DataContainer.instance.mapMovement.y = direction.velocity_Y;

                screenPos.x += direction.velocity_X;
                screenPos.y += direction.velocity_Y;
                player.getPlayerEntity().getPosition().y += direction.velocity_Y;
                player.getPlayerEntity().getPosition().x += direction.velocity_X;

                break;
            case X_LOCKED:
                // Player is locked in Y (inverted)
                transformationMatrix.setTranslate(0, -direction.velocity_Y);
                screenPos.y += direction.velocity_Y;
                player.getPlayerEntity().getPosition().y += direction.velocity_Y;
                DataContainer.instance.mapMovement.y = direction.velocity_Y;

                break;
            case Y_LOCKED:
                // Player is locked in X (inverted)

                transformationMatrix.setTranslate(-direction.velocity_X, 0);
                screenPos.x += direction.velocity_X;
                player.getPlayerEntity().getPosition().x += direction.velocity_X;
                DataContainer.instance.mapMovement.x = direction.velocity_X;


                break;
            case ALL_LOCKED:
                // Map is locked. Player is free
                break;
            default:
                Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
                break;
        }

        transformationMatrix.mapRect(baseRact);
        DataContainer.instance.mapBaseRect = baseRact;
    }


    public RectF getInnerBoarder() {
        return innerBoarder;
    }

    public RectF getOuterBoarder() {
        return outerBoarder;
    }
}
