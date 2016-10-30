package com.graphics;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;

/**
 * Created by thor on 10/16/16.
 */

public class OurGLSurfaceView extends GLSurfaceView {
    private final GlRendere renderer;
    protected Context context;

    public OurGLSurfaceView(Context context) {
        super(context);
        this.context = context;

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the OurGLSurfaceView
        renderer = new GlRendere(context);
        Log.d("OurGLSurfaceView", "Setting renderer");
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(OurGLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        renderer.onResume();
    }
}