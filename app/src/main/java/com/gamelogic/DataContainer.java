package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by PatrickKaalund on 20/11/2016.
 */

public class DataContainer {

    public static Player player;
    public static PointF mapMovement = new PointF(0, 0);
    public static PointF mapGlobalSize = new PointF(0, 0);
    public static RectF mapBaseRect;
    public static Context gameContext;

}
