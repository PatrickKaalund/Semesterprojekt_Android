package com.gamelogic;


import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import com.core.GUpdateable;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.BackgroundFactory;
import com.graphics.EnemySpawner;
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.SpriteEntityFactory;
import com.graphics.Direction;

import java.util.ArrayList;

public class Map extends GUpdateable {

    private DisplayMetrics metrics;
    BackgroundEntity mapBackground;
    BackgroundFactory mapFactory;
    Direction velMap;
    RectF boarder = new RectF(0f, 0f, 2000f, 2000f);
    RectF boarderInder;

    private EnemySpawner enemySpawner;


    public Map(Context c) {
        Log.d("Map", "making map");
        game.objectsToUpdate.add(this);
        this.metrics = c.getResources().getDisplayMetrics();
        mapFactory = new BackgroundFactory(R.drawable.backgrounddetailed2, metrics);
        mapBackground = mapFactory.crateEntity();
        float ratio = metrics.widthPixels / metrics.heightPixels;
        velMap = new Direction();
        boarderInder = new RectF(0f, 0f, metrics.widthPixels - 200, metrics.heightPixels - 200*ratio);
        Log.d("Map", "boarderInder: " + GraphicsTools.rectToString(boarderInder));

        enemySpawner = new EnemySpawner(c);
        enemySpawner.spawnEnemies(100, 10, 10);

    }


    int rotation = 0;

//    @Override
//    public void update() {
//        // read joystick
//        ArrayList<Integer> joystickValues = game.getControl().getJoystickValues();
//
//        int joystick_angle = -joystickValues.get(0);
//        float joystick_strength = ((float) joystickValues.get(1) / 10000);
//
////        Log.d("Player", "Angle: " + joystick_angle);
////        Log.d("Player", "Strength: " + joystick_strength);
//
//        vel.angle = joystick_angle;
//        vel.velocety = joystick_strength;
//
//        mapBackground.moveFrame(vel);
//    }

    @Override
    public void update() {
        // read joystick
        ArrayList<Integer> joystickValues = game.getControl().getJoystickValues();

        int joystick_angle = joystickValues.get(0);
        float joystick_strength = ((float) joystickValues.get(1) / 50);

        velMap.set(joystick_angle, joystick_strength / metrics.heightPixels);

        mapBackground.moveFrame(velMap);
        RectF pboarder = game.getPlayer().getRect();

        if (joystick_strength > 0) {

            if (boarderInder.contains(pboarder.centerX(), pboarder.centerY())) {
//                Log.e("Map", "############ lock ##############");

                mapBackground.setLock(LockDirection.ALL);
//                player.setLock(false);

            } else {
//                Log.e("Map", "############ unlock ##############");
                mapBackground.setLock(LockDirection.ALL);
//                player.setLock(true);
            }

        }
        enemySpawner.update(velMap);
    }


    /**
     * If the rectangle specified by left,top,right,bottom intersects this
     * rectangle, return true and set this rectangle to that intersection,
     * otherwise return false and do not change this rectangle. No check is
     * performed to see if either rectangle is empty. Note: To just test for
     * intersection, use intersects()
     *
     * @param left The left side of the rectangle being intersected with this
     *             rectangle
     * @param top The top of the rectangle being intersected with this rectangle
     * @param right The right side of the rectangle being intersected with this
     *              rectangle.
     * @param bottom The bottom of the rectangle being intersected with this
     *             rectangle.
     * @return true if the specified rectangle and this rectangle intersect
     *              (and this rectangle is then set to that intersection) else
     *              return false and do not change this rectangle.
     */
//    public boolean intersect(,float left, float top, float right, float bottom) {
//        if (this.left < right && left < this.right
//                && this.top < bottom && top < this.bottom) {
//            if (this.left < left) {
//                this.left = left;
//            }
//            if (this.top < top) {
//                this.top = top;
//            }
//            if (this.right > right) {
//                this.right = right;
//            }
//            if (this.bottom > bottom) {
//                this.bottom = bottom;
//            }
//            return true;
//        }
//        return false;
    }

//}


//package com.gamelogic;
//
//
//        import android.content.Context;
//        import android.graphics.Bitmap;
//        import android.graphics.BitmapFactory;
//        import android.graphics.Canvas;
//        import android.graphics.Rect;
//        import android.graphics.drawable.BitmapDrawable;
//        import android.graphics.drawable.Drawable;
//        import android.os.Build;
//        import android.support.annotation.RequiresApi;
//        import android.util.AttributeSet;
//
//        import com.core.GDrawable;
//        import com.core.ScreenDrawer;
//        import com.example.patrickkaalund.semesterprojekt_android.R;
//
//public class Map extends GDrawable {
//
//    private Drawable backgrundImage;
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public Map(Context context, ScreenDrawer screenDrawer) {
//        super(context, screenDrawer);
//        game.objectsToUpdate.add(this);
//        screenDrawer.objectsToDraw.add(this);
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bg_image);
//        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
//        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//        backgrundImage = context.getResources().getDrawable(R.drawable.backgrounddetailed2, null);
//        backgrundImage.setTileModeXY
//    }
//
//    int increase = 0;
//
//    @Override
//    public void draw(Canvas canvas) {
//        Rect imageBounds = canvas.getClipBounds();
//        imageBounds.right += increase;
//        imageBounds.left += increase;
//        imageBounds.top += increase;
//        imageBounds.bottom += increase;
//        imageBounds.union(imageBounds);
//        backgrundImage.setBounds(imageBounds);
//        backgrundImage.draw(canvas);
//    }
//
//    @Override
//    public void update() {
//        increase--;
//    }
//
//
//}
