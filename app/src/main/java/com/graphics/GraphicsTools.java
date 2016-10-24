package com.graphics;

import android.graphics.RectF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by thor on 10/22/16.
 */

public class GraphicsTools {

    /**
     * Gets a float array of the 2D coordinates representing a rectangles
     * corners.
     * The order of the corners in the float array is:
     * 0,1------->1,1
     * ^          |
     * |          v
     * 0,0<-------1,0
     * ^          ^
     * Start      end!
     *
     * @param r the rectangle to get the corners of
     * @return the float array of corners (8 floats)
     */
    public static float[] getCornersFromRect(RectF r) {
        float[] corners = {
                r.left, r.top,
                r.left, r.bottom,
                r.right, r.bottom,
                r.right, r.top,
//                WRONG  (open gl is reversed!!!)
//                  |
//                  v
//                r.left, r.top,
//                r.right, r.top,
//                r.right, r.bottom,
//                r.left, r.bottom
        };
        return corners;
    }


    public static String allVertecisToString(ArrayList<RectF> sprites){
        String r = new String();
        r = "Sprites count: "+sprites.size()+'\n';
        for (RectF s : sprites) {
            r += Arrays.toString(getCornersFromRect(s))+'\n';
        }
        return r;
    }
//    uvs = new float[]{
//                0.0f, 0.0f, // 0,0  --->
//                0.0f, 1.0f,  // 0,1
//                1.0f, 1.0f, // 1,1  --->
//                1.0f, 0.0f, // 1,0  --->
//    };


}
