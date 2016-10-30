package com.gamelogic;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.util.Log;

import com.core.GUpdateable;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.EntityFactory;
import com.graphics.OurGLSurfaceView;

public class Map extends GUpdateable {

    Entity map;
    EntityFactory mapFactory;
    EntityFactory mapFactory2;
    Entity t1,t2,t3;
    Entity f1,f2,f3;

    public Map() {

        Log.d("Map", "making map");
        game.objectsToUpdate.add(this);
//        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        mapFactory = new EntityFactory(R.drawable.player,100,100,2,8,new PointF(50,50));
        mapFactory2 = new EntityFactory(R.drawable.soldier_topdown,100,100,4,2,new PointF(50,50));

        map = mapFactory.crateEntity();
        map.setCurrentSprite(1);
        map.moveBy(500f,500f);
        t1 = mapFactory.crateEntity();
        t1.setCurrentSprite(2);
        t1.moveBy(250f,250f);

        f1 = mapFactory2.crateEntity();
        f2 = mapFactory2.crateEntity();
        f3 = mapFactory2.crateEntity();
        f1.moveBy(100f,100f);


        f2.moveBy(600f,600f);
        f3.moveBy(800f,800f);
        f3.setCurrentSprite(5);
        f2.setCurrentSprite(3);

    }


    int rotation = 0;

    @Override
    public void update() {
//        map.rotate(rotation++);
//        rotation = rotation%360;
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
