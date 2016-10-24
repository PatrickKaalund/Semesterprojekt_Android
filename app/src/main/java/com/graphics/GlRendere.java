package com.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by thor on 10/16/16.
 */

public class GlRendere implements Renderer {
    public static final int TEXTURE_NAME = 0;
    public static final int TEXTURE_SLOT = 1;


    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    GEntity entity = new GEntity(100f, 100f, 2, 8, new PointF(50f, 50f));

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public static float uvs[];//for texture
    public FloatBuffer vertexBuffer;
    public ShortBuffer indexBuffer;
    public FloatBuffer uvBuffer; //for texture

    // Our screenresolution
    float mScreenWidth = 1280;
    float mScreenHeight = 768;
    protected static int textureSlotCount;
    protected static int textureSlot = 0;
    private int currentTexture = 0;

    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;
    ShaderHandler shaderHandler;
    boolean durtyDrawList;
    ArrayList<GEntity> drawList = new ArrayList<>();


    public GlRendere(Context c) {
        mContext = c;
        shaderHandler = new ShaderHandler(mContext);
        mLastTime = System.currentTimeMillis() + 100;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        if (durtyDrawList){
            Collections.sort(drawList);
            durtyDrawList = false;
        }
        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example

        // Render our example
        Render(mtrxProjectionAndView);

        // Save the current time to see how long it took <img src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif" alt=":)" class="wp-smiley"> .
        mLastTime = now;

    }

    private void drawScreen(){

//        for (int i = 0; i < drawList.size(); i++) {
//            currentTexture
//        }

    }

    public void UpdateSprite() {
        // Get new transformed vertices
        vertices = entity.getModel();
//        vertices = sprite.getTransformedVertices();

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }

    private void Render(float[] m) {
        UpdateSprite();
        // clear Screen and Depth Buffer,
        // we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

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
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(shaderHandler.getShaderProgramID(),
                "s_texture");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);

    }


    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {

        // Get the maximum of texture units we can use.
        IntBuffer i = IntBuffer.allocate(1);
        GLES20.glGetIntegerv(GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, i);
        textureSlotCount = i.get(0);
        Log.d("Texture count", "" + i.get(0));

        // Create the triangle


        SetupTriangle();
        entity.makeSprites();

        SetupImage();

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);


        shaderHandler.installShaderFiles();

        entity.moveBy(500f, 500f);
        entity.scale(1.5f);
        entity.rotate(90f);

//        // Create the shaders
//        int vertexShader = shaderHandler.loadShader(GLES20.GL_VERTEX_SHADER, ShaderHandler.VERTEX_SHADER_SOLIDCOLOR);
//        int fragmentShader = shaderHandler.loadShader(GLES20.GL_FRAGMENT_SHADER, ShaderHandler.FRAGMENT_SHADER_SOLID_COLOR);
//
//        ShaderHandler.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
//        GLES20.glAttachShader(ShaderHandler.sp_SolidColor, vertexShader);   // add the vertex shader to program
//        GLES20.glAttachShader(ShaderHandler.sp_SolidColor, fragmentShader); // add the fragment shader to program
//        GLES20.glLinkProgram(ShaderHandler.sp_SolidColor);                  // creates OpenGL ES program executables
//
//        // Set our shader programm
//        GLES20.glUseProgram(ShaderHandler.sp_SolidColor);
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

    public void SetupTriangle() {

        // Get information of sprite.
//        vertices = sprite.getTransformedVertices();
        vertices = entity.getModel();

        // The order of vertexrendering for a quad
        indices = new short[]{0, 1, 2, 0, 2, 3};

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indexBuffer = dlb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);


//        // We have create the vertices of our view.
//        vertices = new float[]
//
////                {
////                        10.0f, 200f, 0.0f,
////                        10.0f, 100f, 0.0f,
////                        100f, 100f, 0.0f,
////                };
//
//                {
//                        //Vertecie must be declared counterclockwise order
//                        //it doesent matter in wich you start with
//                        300.0f/*x*/, 600f/*y*/, 0.0f/*z*/,
//                        300.0f, 400f, 0.0f,
//                        400f, 400f, 0.0f,
//                        400f, 600f, 0.0f,
//                };
//
//        //Indexbuffer must be declared counterclockwise order same order as verticebuffer
//        indices = new short[]{// The order of vertexrendering.
//                0, 1, 2, //first triangle
//                0, 2, 3  //second triangle etc.
//        };
//        // The vertex buffer.
//        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
//
//        // initialize byte buffer for the draw list
//        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        indexBuffer = dlb.asShortBuffer();
//        indexBuffer.put(indices);
//        indexBuffer.position(0);

    }

    /**
     * TODO CHECK TEXTURE SPACE!!!!!!!!
     *
     * @param bmp
     * @return texturenames[TEXTURE_NAME, TEXTURE_SLOT]
     */
    protected static int[] loadTextrue(Bitmap bmp) {

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[2];

        GLES20.glGenTextures(1, texturenames, 0);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + textureSlot);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[TEXTURE_NAME]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);

        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
        texturenames[TEXTURE_SLOT] = textureSlot;
        textureSlot++;
        return texturenames;
    }

    public void SetupImage() {

        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player);
        entity.loadTextrue(bmp);
        // Create our UV coordinates.
//        uvs = (new float[]{
//                0.0f, 0.0f, //  0,0  ---> left, top
//                0.0f, 0.5f,  //  1,0  ---> left, bot
//                0.125f, 0.5f, // 1,1  ---> right, bot
//                0.125f, 0.0f, // 0,1  ---> right, top
//
//
//        });

//        uvs = new float[]{
//                0.0f, 0.0f, // 0,0  ---> left, top
//                0.0f, 1.0f,  // 1,0  ---> left, bot
//                1.0f, 1.0f, // 1,1  ---> right, bot
//                1.0f, 0.0f, // 0,1  ---> right, top
//        };
        Log.d("uvs direct", Arrays.toString(uvs));

        uvs = entity.getSpriteUvs();
        Log.d("uvs from sprite", entity.spritesToString());


        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

//        // Generate Textures, if more needed, alter these numbers.
//        int[] texturenames = new int[1];
//        GLES20.glGenTextures(1, texturenames, 0);
//
//        // Retrieve our image from resources.
//
//
//        // Temporary create a bitmap
//
////        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.skate1);
//        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.player);
//        // Bind texture to texturename
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
//
//        // Set filtering
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
////        // Set wrapping mode
////        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
////        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
//
//        // Load the bitmap into the bound texture.
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
//
//        // We are done using the bitmap so we should recycle it.
//        bmp.recycle();

    }

    public void addEntety(GEntity entity) {
        durtyDrawList = true;
        drawList.add(entity);
    }

//    public void SetupTriangle() {
//        // We will need a randomizer
//        Random rnd = new Random();
//
//        // Our collection of vertices
//        vertices = new float[30 * 4 * 3];
//
//        // Create the vertex data
//        for (int i = 0; i < 30; i++) {
//            int offset_x = rnd.nextInt((int) swp);
//            int offset_y = rnd.nextInt((int) shp);
//
//            // Create the 2D parts of our 3D vertices, others are default 0.0f
//            vertices[(i * 12) + 0] = offset_x;
//            vertices[(i * 12) + 1] = offset_y + (30.0f * ssu);
//            vertices[(i * 12) + 2] = 0f;
//            vertices[(i * 12) + 3] = offset_x;
//            vertices[(i * 12) + 4] = offset_y;
//            vertices[(i * 12) + 5] = 0f;
//            vertices[(i * 12) + 6] = offset_x + (30.0f * ssu);
//            vertices[(i * 12) + 7] = offset_y;
//            vertices[(i * 12) + 8] = 0f;
//            vertices[(i * 12) + 9] = offset_x + (30.0f * ssu);
//            vertices[(i * 12) + 10] = offset_y + (30.0f * ssu);
//            vertices[(i * 12) + 11] = 0f;
//        }
//
//        // The indices for all textured quads
//        indices = new short[30 * 6];
//        int last = 0;
//        for (int i = 0; i < 30; i++) {
//            // We need to set the new indices for the new quad
//            indices[(i * 6) + 0] = (short) (last + 0);
//            indices[(i * 6) + 1] = (short) (last + 1);
//            indices[(i * 6) + 2] = (short) (last + 2);
//            indices[(i * 6) + 3] = (short) (last + 0);
//            indices[(i * 6) + 4] = (short) (last + 2);
//            indices[(i * 6) + 5] = (short) (last + 3);
//
//            // Our indices are connected to the vertices so we need to keep them
//            // in the correct order.
//            // normal quad = 0,1,2,0,2,3 so the next one will be 4,5,6,4,6,7
//            last = last + 4;
//        }
//
//        // The vertex buffer.
//        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        vertexBuffer = bb.asFloatBuffer();
//        vertexBuffer.put(vertices);
//        vertexBuffer.position(0);
//
//        // initialize byte buffer for the draw list
//        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        drawListBuffer = dlb.asShortBuffer();
//        drawListBuffer.put(indices);
//        drawListBuffer.position(0);
//    }
//
//    public void SetupImage() {
//        // We will use a randomizer for randomizing the textures from texture atlas.
//        // This is strictly optional as it only effects the output of our app,
//        // Not the actual knowledge.
//        Random rnd = new Random();
//
//        // 30 imageobjects times 4 vertices times (u and v)
//        uvs = new float[30 * 4 * 2];
//
//        // We will make 30 randomly textures objects
//        for (int i = 0; i < 30; i++) {
//            int random_u_offset = rnd.nextInt(2);
//            int random_v_offset = rnd.nextInt(2);
//
//            // Adding the UV's using the offsets
//            uvs[(i * 8) + 0] = random_u_offset * 0.5f;
//            uvs[(i * 8) + 1] = random_v_offset * 0.5f;
//            uvs[(i * 8) + 2] = random_u_offset * 0.5f;
//            uvs[(i * 8) + 3] = (random_v_offset + 1) * 0.5f;
//            uvs[(i * 8) + 4] = (random_u_offset + 1) * 0.5f;
//            uvs[(i * 8) + 5] = (random_v_offset + 1) * 0.5f;
//            uvs[(i * 8) + 6] = (random_u_offset + 1) * 0.5f;
//            uvs[(i * 8) + 7] = random_v_offset * 0.5f;
//        }
//
//        // The texture buffer
//        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        uvBuffer = bb.asFloatBuffer();
//        uvBuffer.put(uvs);
//        uvBuffer.position(0);
//
//        // Generate Textures, if more needed, alter these numbers.
//        int[] texturenames = new int[1];
//        GLES20.glGenTextures(1, texturenames, 0);
//
//        // Retrieve our image from resources.
//        int id = mContext.getResources().getIdentifier("drawable/textureatlas", null, mContext.getPackageName());
//
//        // Temporary create a bitmap
//        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);
//
//        // Bind texture to texturename
//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);
//
//        // Set filtering
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
//        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
//
//        // Load the bitmap into the bound texture.
//        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
//
//        // We are done using the bitmap so we should recycle it.
//        bmp.recycle();
//
//    }


//    class Sprite {
//        float angle;
//        float scale;
//        RectF base;
//        PointF translation;
//
//        public Sprite() {
//            // Initialise our intital size around the 0,0 point
//            base = new RectF(-50f, 50f, 50f, -50f);
//
//            // Initial currentPos
//            translation = new PointF(50f, 50f);
//
//            // We start with our inital size
//            scale = 1f;
//
//            // We start in our inital angle
//            angle = 0f;
//        }
//
//
//        public void translate(float deltax, float deltay) {
//            // Update our location.
//            translation.x += deltax;
//            translation.y += deltay;
//        }
//
//        public void scale(float deltas) {
//            scale += deltas;
//        }
//
//        public void rotate(float deltaa) {
//            angle += deltaa;
//        }
//
//        public float[] getTransformedVertices() {
//            // Start with scaling
//            float x1 = base.left * scale;
//            float x2 = base.right * scale;
//            float y1 = base.bottom * scale;
//            float y2 = base.top * scale;
//
//            // We now detach from our Rect because when rotating,
//            // we need the seperate points, so we do so in opengl order
//            PointF one = new PointF(x1, y2);
//            PointF two = new PointF(x1, y1);
//            PointF three = new PointF(x2, y1);
//            PointF four = new PointF(x2, y2);
//
//            // We create the sin and cos function once,
//            // so we do not have calculate them each time.
//            float s = (float) Math.sin(angle);
//            float c = (float) Math.cos(angle);
//
//            // Then we rotate each point
//            one.x = x1 * c - y2 * s;
//            one.y = x1 * s + y2 * c;
//            two.x = x1 * c - y1 * s;
//            two.y = x1 * s + y1 * c;
//            three.x = x2 * c - y1 * s;
//            three.y = x2 * s + y1 * c;
//            four.x = x2 * c - y2 * s;
//            four.y = x2 * s + y2 * c;
//
//            // Finally we translate the sprite to its correct position.
//            one.x += translation.x;
//            one.y += translation.y;
//            two.x += translation.x;
//            two.y += translation.y;
//            three.x += translation.x;
//            three.y += translation.y;
//            four.x += translation.x;
//            four.y += translation.y;
//
//            // We now return our float array of vertices.
//            return new float[]
//                    {
//                            one.x, one.y, 0.0f,
//                            two.x, two.y, 0.0f,
//                            three.x, three.y, 0.0f,
//                            four.x, four.y, 0.0f,
//                    };
//        }
//    }


}

