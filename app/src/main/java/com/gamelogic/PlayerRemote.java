package com.gamelogic;

import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;
import com.network.Firebase.RemotePlayer;
import com.network.Firebase.NetworkHandler;

import android.graphics.PointF;
import android.graphics.RectF;
import android.provider.ContactsContract;
import android.util.Log;


public class PlayerRemote extends PlayerCommon implements RemotePlayer {

    private Entity remotePlayer;
    private float xOld, yOld, xNew, yNew, angle;
    private Direction direction;
    private BackgroundEntity map;

    private float offset = 0;

    public PlayerRemote(NetworkHandler networkHandler, BackgroundEntity map) {
//        game.objectsToUpdate.add(this);
        this.map = map;

        networkHandler.addPlayerListener(this);

        remotePlayer = super.player;

        // Place remotePlayer 250 right from local player
        placeElementFromGlobalPos(new PointF(DataContainer.instance.player.getPos().x + offset, DataContainer.instance.player.getPos().y));

        xNew = xOld = DataContainer.instance.player.getPos().x;
        yNew = yOld = DataContainer.instance.player.getPos().y;

        direction = new Direction();
    }

    private void placeElementFromGlobalPos(PointF globalPos) {
        PointF posOnScreen = new PointF();
        posOnScreen.x = globalPos.x - map.screenPos.x + DataContainer.instance.gameContext.getResources().getDisplayMetrics().widthPixels / 2;
        posOnScreen.y = globalPos.y - map.screenPos.y + DataContainer.instance.gameContext.getResources().getDisplayMetrics().heightPixels / 2;
        this.remotePlayer.placeAt(posOnScreen.x, posOnScreen.y);
        this.remotePlayer.setPosition(new PointF(globalPos.x, globalPos.y));
        this.remotePlayer.setAngle(angle);

//        Log.e("PlayerRemote", "PosOnScreen: " + posOnScreen.x + ", " + posOnScreen.y +
//                ". Player pos: " + DataContainer.player.getPos().x + ", " + DataContainer.player.getPos().y +
//                ". Remote player pos: " + this.remotePlayer.getPosition().x + ", " + this.remotePlayer.getPosition().y +
//                ". Map pos: " + map.screenPos.x + ", " + map.screenPos.y);
    }


    public void update() {
//        Log.e("PlayerRemote", "New pos: " + xNew + ", " + yNew + ". Old pos: " + xOld + ", " + yOld);

        placeElementFromGlobalPos(new PointF(xNew, yNew));
        this.remotePlayer.drawNextSprite();

//        if (xNew == xOld || yNew == yOld) {
////            Log.e("PlayerRemote", "New pos: " + xNew + ", " + yNew + ". Old pos: " + xOld + ", " + yOld);
//            xOld = xNew;
//            yOld = yNew;
//
//            this.remotePlayer.drawNextSprite();
//        }else{
//            this.remotePlayer.setCurrentSprite(0);
//        }
    }

    @Override
    public void updatePlayerPosition(float centerX, float centerY, int angle) {
//        Log.e("PlayerRemote", "New pos: " + centerX + ", " + centerY + " with angle: " + angle);

        centerX += offset;
        this.angle = angle;
        xNew = centerX;
        yNew = centerY;

//        this.angle = (float) Math.toDegrees(Math.atan2(yNew - yOld, xNew - xOld));
//        if (this.angle < 0) {
//            this.angle += 360;
//        }
    }

    public RectF getRect() {
        return this.remotePlayer.getRect();
    }

    @Override
    public boolean doDamage(int damge) {
        super.health -= damge;
        return true;
    }
}

