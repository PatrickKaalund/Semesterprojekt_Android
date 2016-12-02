package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.graphics.Direction;

public class DirectionLock {
    public static final int UNLOCKED = 0;
    public static final int X_LOCKED = 2;
    public static final int Y_LOCKED = 1;
    public static final int ALL_LOCKED = 3;

    private static final int T_BITPOS = 3;
    private static final int B_BITPOS = 2;
    private static final int L_BITPOS = 1;
    private static final int R_BITPOS = 0;

    private static final int X_BITPOS = 1;
    private static final int Y_BITPOS = 0;

    private static final int L = 2;
    private static final int R = 1;
    private static final int T = 8;
    private static final int B = 4;
    private static final int BL = 6;
    private static final int BR = 5;
    private static final int TR = 9;
    private static final int TL = 10;

    private int tblr = 0;
    private int lockXY = 0;

    public DirectionLock() {

    }

    //v0 (v <= 90)
    //v1 (v >= 90 && v <= 180)
    //v2 (v >= 180 && v <= 270)
    //v3 (v >= 270)
    int old_tblr;

    private void checkTBLR(Direction direction, RectF ref, float posX, float posY, int tblrIn) {
//        if (direction.tag == 2) {
//            LL(this, "::::MAP::::Boarder: " + ref.toString() + '\n' +
//                    ". Position: " + posX + ", " + posY + '\n' +
//                    ". Velocity: " + direction.velocity_X + '\n' + ", " + direction.velocity_Y + '\n' +
//                    ".Angle " + direction.getAngle() + '\n' +   " old_tblr "+old_tblr+ '\n');
//        }
//        if (direction.tag == 1) {
//            LL(this, "::::PLY::::Boarder: " + ref.toString() + '\n' +
//                    ". Position: " + posX + ", " + posY + '\n' +
//                    ". Velocity: " + direction.velocity_X + ", " + direction.velocity_Y + '\n' +
//                    ".Angle " + direction.getAngle() + '\n' + '\n'
//
//            );
//        }


        float deltaT = ref.top - posY;
        float deltaB = ref.bottom - posY;
        float deltaR = ref.right - posX;
        float deltaL = ref.left - posX;

        //BL
        if ((old_tblr == BL) && ((tblrIn == BL) || (tblrIn == L) || (tblrIn == B))) {
            direction.velocity_Y = 0;
            direction.velocity_X = 0;
            tblr = old_tblr;

            return;
        }//BR
        else if ((old_tblr == BR) && ((tblrIn == BR) || (tblrIn == B) || (tblrIn == R))) {
            direction.velocity_Y = 0;
            direction.velocity_X = 0;
            tblr = old_tblr;
            return;
        }//TR
        else if ((old_tblr == TR) && ((tblrIn == TR) || (tblrIn == T) || (tblrIn == R))) {
            direction.velocity_Y = 0;
            direction.velocity_X = 0;
            tblr = old_tblr;

            return;
        }//TL
        else if ((old_tblr == TL) && ((tblrIn == TL) || (tblrIn == L) || (tblrIn == T))) {
            direction.velocity_Y = 0;
            direction.velocity_X = 0;
            tblr = old_tblr;

            return;
        }


        if ((deltaT < direction.velocity_Y)) {
            tblr |= (1 << T_BITPOS);
            direction.velocity_Y = deltaT;
        }
        if ((deltaB > direction.velocity_Y)) {//
            tblr |= (1 << B_BITPOS);
            direction.velocity_Y = deltaB;
        }
        if ((deltaR < direction.velocity_X)) {//Rigth
            tblr |= (1 << R_BITPOS);
            direction.velocity_X = deltaR;
        }
        if ((deltaL > direction.velocity_X)) {//LEft
            tblr |= (1 << L_BITPOS);
            direction.velocity_X = deltaL;
        }
//        if (direction.tag == 2) {
//
//            LL(this, "::::MAP::::Adjusted velocity: " + direction.velocity_X + ", " + direction.velocity_Y);
//
//            LL(this, "::::MAP::::tblr: " + tblr);
//
//        }
//        if (direction.tag == 1) {
//
//            LL(this, "::::PLY::::Adjusted velocity: " + direction.velocity_X +  ", " + direction.velocity_Y);
//
//            LL(this, "::::PLY::::tblr: " + tblr);
//        }


    }

    private int check(Direction direction, RectF ref, float posX, float posY, int tblrIn) {
        tblr = 0;
        lockXY = 0;
        int angle = direction.getAngle();
        checkTBLR(direction, ref, posX, posY, tblrIn);

        old_tblr = tblr;
        switch (tblr) {
            // S0 - Free
            case 0b0000:
                lockXY = 0b00;
                break;
            // S1 - R
            case R:
                //lockXY |= ((angle > 90 && angle < 270) ? 1 : 0) << X_BITPOS;
                if (angle <= 90 || angle >= 270) lockXY = 0b10;
                break;
            // S2 - L
            case L:
                //lockXY |= ((angle >= 90 && angle <= 270) ? 1 : 0) << X_BITPOS;
                if (angle >= 90 && angle <= 270) lockXY = 0b10;
                break;
            // S3 - B
            case B:
                if (angle >= 180) lockXY = 0b01;
                break;
            // S4 - BR
            case BR:
//                if(angle <= 90) lockXY = 0b10;
//                else if(angle >= 180 && angle <= 270) lockXY = 0b01;
//                else if(angle >= 270) lockXY = 0b11;

                lockXY |= (((angle <= 90 || angle >= 270) ? 1 : 0) << X_BITPOS);
                lockXY |= ((angle >= 180 ? 1 : 0) << Y_BITPOS);
                break;
            // S5 - BL
            case BL:
                if (angle > 90 && angle < 180) lockXY = 0b10;
                else if (angle >= 180 && angle <= 270) lockXY = 0b11;
                else if (angle >= 270) lockXY = 0b01;

//                lockXY |= (((angle >= 90 && angle <= 270) ? 1 : 0) << X_BITPOS);
//                lockXY |= ((angle >= 180 ? 1 : 0) << Y_BITPOS);
                break;
            // S6 - T
            case T:
                if (angle <= 180) lockXY = 0b01;
                break;
            // S7 - TR
            case TR:
                lockXY |= (((angle <= 90 || angle >= 270) ? 1 : 0) << X_BITPOS);
                lockXY |= ((angle <= 180 ? 1 : 0) << Y_BITPOS);
                break;
            // S8 - TL
            case TL:
                lockXY |= (((angle >= 90 && angle <= 270) ? 1 : 0) << X_BITPOS);
                lockXY |= ((angle <= 180 ? 1 : 0) << Y_BITPOS);
                break;
            default:
                Log.e(this.getClass().getCanonicalName(), "Defaulted in DirectionLock::check(). State is: " + tblr);
        }
        return lockXY;
    }

    public int check(Direction direction, RectF ref, float posX, float posY) {
        return check(direction, ref, posX, posY, 0);
    }

    public int check(Direction direction, RectF ref, PointF pos, int tblrIn) {
        return check(direction, ref, pos.x, pos.y, tblrIn);
    }

//    public int check(Direction direction, RectF ref, float posX, float posY){
//        return check(direction, ref, posX, posY, 0);
//
//    }

//    public int check(Direction direction, RectF ref, float posX, float posY, int tblrIn)


    public boolean isLockedX() {
        return lockXY == 0b10;
    }

    public boolean isLockedY() {
        return lockXY == 0b01;
    }

    public int getLockXY() {
        return lockXY;
    }

    int getTblr() {
        return tblr;
    }
}
