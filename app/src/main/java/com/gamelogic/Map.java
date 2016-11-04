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
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.SpriteEntityFactory;
import com.graphics.Direction;

import java.util.ArrayList;

public class Map extends GUpdateable {

    private final SpriteEntityFactory playerFactory;
    private final Entity player;
    private DisplayMetrics metrics;
    BackgroundEntity mapBackground;
    BackgroundFactory mapFactory;
    //    Direction vel = new Direction();
    Direction velMap;
    Direction velPlayer;
    RectF boarder = new RectF(0f, 0f, 2000f, 2000f);
    RectF boarderInder;


    public Map(Context c) {
        Log.d("Map", "making map");
        game.objectsToUpdate.add(this);
        this.metrics = c.getResources().getDisplayMetrics();
        mapFactory = new BackgroundFactory(R.drawable.backgrounddetailed2, metrics);
        mapBackground = mapFactory.crateEntity();
        playerFactory = new SpriteEntityFactory(R.drawable.soldier_topdown_adjusted,
                200, 200, 4, 2, new PointF(metrics.widthPixels / 2, metrics.heightPixels / 2));

        player = playerFactory.createEntity();
        player.setCurrentSprite(0);
        player.setAngleOffSet(90);
        velMap = new Direction();
        velPlayer = new Direction();
        boarderInder = new RectF(0f, 0f, metrics.widthPixels - 200, metrics.heightPixels - 200);
        Log.d("Map", "boarderInder: " + GraphicsTools.rectToString(boarderInder));


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
        float joystick_strength = ((float) joystickValues.get(1) / 5);

        velPlayer.set(joystick_angle, joystick_strength);
        velMap.set(joystick_angle, joystick_strength / metrics.heightPixels);

        mapBackground.moveFrame(velMap);
        RectF pboarder = player.move(velPlayer);

        if (joystick_strength > 0) {

            if (boarderInder.contains(pboarder.centerX(), pboarder.centerY())) {
                Log.e("Map", "############ lock ##############");

                mapBackground.setLock(false);
                player.setLock(true);
            } else {
                Log.e("Map", "############ unlock ##############");

                mapBackground.setLock(true);
                player.setLock(false);
            }

        }
    }

}


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
