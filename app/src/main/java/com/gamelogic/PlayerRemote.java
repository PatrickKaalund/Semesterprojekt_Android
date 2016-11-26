package com.gamelogic;

import com.graphics.Entity;
import com.network.Firebase.RemotePlayer;
import com.network.Firebase.NetworkHandler;
import android.graphics.RectF;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public class PlayerRemote extends PlayerCommon implements RemotePlayer {

    private Entity remotePlayer;
    private float xOld, yOld, xNew, yNew, angle;

    public PlayerRemote(NetworkHandler networkHandler) {
//        game.objectsToUpdate.add(this);

        networkHandler.addPlayerListener(this);

        remotePlayer = super.player;
        remotePlayer.placeAt(650, 400);
        xOld = yOld = xNew = yNew = 0;
    }

    public void update() {

        // Should not be here :-)
//        game.map.move(remotePlayer, direction);

        if (xNew != xOld || yNew != yOld) {
            remotePlayer.drawNextSprite();
            remotePlayer.setAngleOffSet(90 + angle);
            xOld = xNew;
            yOld = yNew;
        } else {
            remotePlayer.setCurrentSprite(0);
        }
    }

    @Override
    public void updatePlayerPosition(float centerX, float centerY) {
        centerX += 250;
        //remotePlayer.placeAt(centerX, centerY);
        remotePlayer.placeAt(1000, 1000);
        xNew = centerX;
        yNew = centerY;

        this.angle = (float) Math.toDegrees(Math.atan2(yNew - yOld, xNew - xOld));
        if (this.angle < 0) {
            this.angle += 360;
        }

//        Log.d("PlayerRemote", "New pos: " + centerX + ", " + centerY + " with angle: " + angle);
    }

    public RectF getRect() {
        return this.remotePlayer.getRect();
    }

    @Override
    public void doDamage(int damge) {
        super.health -= damge;
    }
}

