package com.graphics;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by thor on 10/16/16.
 */

public class OurGLSurfaceView extends GLSurfaceView {
    private final GlRendere mRenderer;

    public OurGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the OurGLSurfaceView
        mRenderer = new GlRendere(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(OurGLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mRenderer.onPause();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mRenderer.onResume();
    }
}
