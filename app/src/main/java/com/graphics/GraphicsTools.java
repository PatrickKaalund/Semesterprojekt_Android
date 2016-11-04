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
        float[] corners = new float[]{
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

//
//    /**
//     * Gets a float array of the 2D coordinates representing a rectangles
//     * corners.
//     * The order of the corners in the float array is:
//     * 0,1------->1,1
//     * ^          |
//     * |          v
//     * 0,0<-------1,0
//     * ^          ^
//     * Start      end!
//     *
//     * @param r the rectangle to get the corners of
//     * @return the float array of corners (8 floats)
//     */
//    public static float[] getCornersFromRect(RectF r) {
//        float[] corners = new float[]{
//                r.left, r.top,
//                r.right, r.top,
//                r.right, r.bottom,
//                r.left, r.bottom
//        };
//        return corners;
//    }


//    /**
//     * Gets a float array of the 2D coordinates representing a rectangles
//     * corners.
//     * The order of the corners in the float array is:
//     * 0,1------->1,1
//     * ^          |
//     * |          v
//     * 0,0<-------1,0
//     * ^          ^
//     * Start      end!
//     *
//     * @param r the rectangle to get the corners of
//     * @return the float array of corners (8 floats)
//     */
//    public static float[] getCornersFromRectNom(RectF r) {
//        float[] corners = new float[]{
//                r.left, r.top,
//                r.right, r.top,
//                r.right, r.bottom,
//                r.left, r.bottom
//        };
//        return corners;
//    }


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
     * @param p the rectangle to get the corners of
     * @return the float array of corners (8 floats)
     */
    public static RectF setCornersToRect(float[] p) {
        RectF r = new RectF();
        //float[] p = {200, 450, 600, 450, 600, 300, 200, 300};
        r.set(p[0],p[1],p[4],p[5]);
        return r;
    }

//    /**
//     * Gets a float array of the 2D coordinates representing a rectangles
//     * corners.
//     * The order of the corners in the float array is:
//     * 0,1------->1,1
//     * ^          |
//     * |          v
//     * 0,0<-------1,0
//     * ^          ^
//     * Start      end!
//     *
//     * @param p the rectangle to get the corners of
//     * @return the float array of corners (8 floats)
//     */
//    public static RectF setCornersToRectNorm(float[] p) {
//        RectF r = new RectF();
//        r.set(p[0],p[1],p[4],p[5]);
////        r.left, r.top,
////                r.right, r.top,
////                r.right, r.bottom,
////                r.left, r.bottom
//        return r;
//    }

    public static float[] getCornersFromRectWithZ(RectF r) {
        float[] p = getCornersFromRect(r);
        float[] px = {p[0], p[1], 0f, p[2], p[3], 0f, p[4], p[5], 0f, p[6], p[7], 0f};

//        float[] corners = {
//                r.left, r.top, 0f,
//                r.left, r.bottom, 0f,
//                r.right, r.bottom, 0f,
//                r.right, r.top, 0f,
//        };
        return px;
    }

    public static String rectToString(RectF r) {
        return Arrays.toString(getCornersFromRect(r));
    }
    public static String rectToStringWithZ(RectF r) {
        return Arrays.toString(getCornersFromRectWithZ(r));
    }

    public static String allVertecisToString(ArrayList<RectF> sprites) {
        String r = new String();
        r = "Sprites count: " + sprites.size() + '\n';
        for (RectF s : sprites) {
            r += Arrays.toString(getCornersFromRect(s)) + '\n';
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
