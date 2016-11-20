package com.gamelogic;

import android.graphics.PointF;

/**
 * Created by PatrickKaalund on 20/11/2016.
 */

public class DataContainer {

    private static Player player;

    public PointF getPlayerPos(){ return this.player.getPos(); }
    public Player getPlayer(){ return this.player; }
    public void setPlayer(Player player){ this.player = player; }
}
