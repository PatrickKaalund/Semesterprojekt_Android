package com.graphics;

import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by thor on 11/10/16.
 */

public class RectMaster extends RectF {

    public RectMaster() {
    }

    public RectMaster(float left, float top, float right, float bottom) {
        super(left, top, right, bottom);
    }

    public RectMaster(RectF r) {
        super(r);
    }

    public RectMaster(Rect r) {
        super(r);
    }

//    public Direction collision(float x, float y) {
//        return left < right && top < bottom  // check for empty first
//                && x >= left && x < right && y >= top && y < bottom;
//    }

}
