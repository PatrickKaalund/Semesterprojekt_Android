package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;


public class DataContainer {

    public static DataContainer instance = new DataContainer();
    public Player player;
    public PointF mapMovement = new PointF(0, 0);
    public PointF mapGlobalSize = new PointF(0, 0);
    public RectF mapBaseRect;
    public Context gameContext;
    public boolean multiplayerGame = false;

}
