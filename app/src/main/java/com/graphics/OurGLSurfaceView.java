package com.graphics;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;

public class OurGLSurfaceView extends GLSurfaceView {
    private final GlRenderer renderer;
    protected Context context;

    public OurGLSurfaceView(Context context) {
        super(context);
        this.context = context;

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        // Set the Renderer for drawing on the OurGLSurfaceView
        renderer = new GlRenderer(context);
        Log.d("OurGLSurfaceView", "OurGLSurfaceView constructer: Setting renderer");
        setRenderer(renderer);
        // Render the view only when there is a change in the drawing data
        setRenderMode(OurGLSurfaceView.RENDERMODE_WHEN_DIRTY);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        renderer.onPause();
        Log.d("OurGLSurfaceView", "onPause");

    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("OurGLSurfaceView", "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        renderer.onResume();
        Log.d("OurGLSurfaceView", "onResume");

    }
}
