package com.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by thor on 10/16/16.
 */

public class GlRendere implements Renderer {
    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
//    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public FloatBuffer uvBuffer; //for texture

    // Our screenresolution
    float mScreenWidth = 1280;
    float mScreenHeight = 768;
    protected static int textureSlotCount;
    protected static int textureSlot = 0;
    private int currentTexture = 0;
    private int currentModelBufferSize = 0;

    // Misc
    Context context;
    long mLastTime;
    ShaderHandler shaderHandler;
    protected static boolean durtyDrawList;
    protected static ArrayList<EntityFactory> drawList = new ArrayList<>();
    protected static int totalModelCount = 0;

    public GlRendere(Context c) {
        context = c;
        shaderHandler = new ShaderHandler(context);
        mLastTime = System.currentTimeMillis() + 100;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    /**
     * Allocate bytes for "vertexBuffer","indexBuffer","uvBuffer"
     * The buffers will be able to hold the amount of models corresponding to
     * "modelsToAllocate" after this call
     * @param modelsToAllocate
     */
    private void allocateBuffers(int modelsToAllocate){
        currentModelBufferSize = modelsToAllocate;
        // The vertex buffer.
        //4 vertices of 3 coordinates each x,y,z
        ByteBuffer bb = ByteBuffer.allocateDirect((modelsToAllocate * 4 * 3) * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        // The texture buffer
        ByteBuffer uvsbb = ByteBuffer.allocateDirect((modelsToAllocate * 4 * 2) * 4);
        uvsbb.order(ByteOrder.nativeOrder());
        uvBuffer = uvsbb.asFloatBuffer();
        // initialize byte buffer for the draw list (indexBuffer)
        ByteBuffer dlb = ByteBuffer.allocateDirect((modelsToAllocate *6) * 2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        //Fill the Index buffer. This is the same for all models. Only done once per buffer increase
        int last = 0;
        for (int i = 0; i < modelsToAllocate; i++) {
            // We need to set the new indices for the new quads
            indexBuffer.put((short) (last + 0));
            indexBuffer.put((short) (last + 1));
            indexBuffer.put((short) (last + 2));
            indexBuffer.put((short) (last + 0));
            indexBuffer.put((short) (last + 2));
            indexBuffer.put((short) (last + 3));
            // Our indices are connected to the vertices so we need to keep them
            // in the correct order.
            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
            last += 4;
        }
        Log.d("Renderer", "############# ALLOCATING BUFFERS ##########\n" +
                "Allocating models : "+ modelsToAllocate+'\n'+
                "Allocating bytes  : "+ (((modelsToAllocate * 4 * 3) * 4)+ ((modelsToAllocate *6) * 2)+(modelsToAllocate * 6)));
    }


    /**
     * Render a fra frame
     * @param unused
     */
    @Override
    public void onDrawFrame(GL10 unused) {
        FPSMeasuring.counter++;
        // clear Screen and Depth Buffer,
        // we have set the clear color as almost black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        for (EntityFactory ef : drawList) {
            if (!ef.textureLoaded) loadTextrue(ef);//Check if the texture is loaded (only once per factory)
            if (ef.entityDrawCount == 0) continue; //Continue if there is nothing to draw
            //Check if we need to encase buffer size. Current buffer size
            // is allays as big as the biggest factory.
            // TODO maybe decease buffer size at some point???
            if (ef.entityDrawCount > currentModelBufferSize){
                allocateBuffers(ef.entityDrawCount);
            }
            currentTexture = ef.textureID;

            int draw = 0;
            GraphicInternEntity e;
            // Create the vertex data
            for (int i = 0; i < ef.entityDrawCount; i++) {
                e = ef.productionLine.get(draw++);
                while (!e.mustDrawThis()) {//Find next model to draw
                    e = ef.productionLine.get(draw++);
                }
                //Add the model data to buffers
                vertexBuffer.put(e.getModel());
                uvBuffer.put(e.getSpriteUvs());
            }
            vertexBuffer.position(0);
            indexBuffer.position(0);
            uvBuffer.position(0);

            //========== Do the rendering ============
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            // get handle to vertex shader's vPosition member
            int mPositionHandle =
                    GLES20.glGetAttribLocation(shaderHandler.getShaderProgramID(), "vPosition");

            // Enable generic vertex attribute array
            GLES20.glEnableVertexAttribArray(mPositionHandle);

            // Prepare the triangle coordinate data
            GLES20.glVertexAttribPointer(mPositionHandle, 3,
                    GLES20.GL_FLOAT, false,
                    0, vertexBuffer);
            // Get handle to texture coordinates location
            int mTexCoordLoc = GLES20.glGetAttribLocation(shaderHandler.getShaderProgramID(),
                    "a_texCoord");

            // Enable generic vertex attribute array
            GLES20.glEnableVertexAttribArray(mTexCoordLoc);

            // Prepare the texturecoordinates
            GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                    false,
                    0, uvBuffer);

            // Get handle to shape's transformation matrix
            int mtrxhandle = GLES20.glGetUniformLocation(shaderHandler.getShaderProgramID(),
                    "uMVPMatrix");

            // Apply the projection and view transformation
            GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, mtrxProjectionAndView, 0);

            // Get handle to textures locations
            int mSamplerLoc = GLES20.glGetUniformLocation(shaderHandler.getShaderProgramID(),
                    "s_texture");

            // Set the sampler texture unit to 0, where we have saved the texture.
            GLES20.glUniform1i(mSamplerLoc, currentTexture);

            // Draw the triangle
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, ef.entityDrawCount*6,//<-- indices.length for current factory
                    GLES20.GL_UNSIGNED_SHORT, indexBuffer);

            // Disable vertex array
            GLES20.glDisableVertexAttribArray(mPositionHandle);
            GLES20.glDisableVertexAttribArray(mTexCoordLoc);

        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {

        // Get the maximum of texture units we can use.
        IntBuffer i = IntBuffer.allocate(1);
        GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, i);
        textureSlotCount = i.get(0);
        Log.d("Texture count", "" + i.get(0));

        // Set the clear color to black
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1);
        shaderHandler.installShaderFiles();
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        // Clear our matrices
        for (int i = 0; i < 16; i++) {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite currentPos.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

    }

    /**
     * Load textures to the video mem
     * @param ef
     */
    private void loadTextrue(EntityFactory ef) {

        // Generate Textures
        int[] texturename = new int[1];

        GLES20.glGenTextures(1, texturename, 0);
        ef.textureID = textureSlot;
        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureSlot);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturename[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), ef.bitMapID);
        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        Log.d("Renderer", "Texture loaded at slot "+textureSlot);
        textureSlot++;
        ef.textureLoaded = true;
    }


}


