package com.graphics;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by thor on 10/22/16.
 */

public class GEntity implements Comparable<GEntity> {
    private final int textureAtlasRows;
    private final int textureAtlasColumns;
    private final float height, width;
    ArrayList<RectF> sprites = new ArrayList<>();
    ArrayList<Integer> drawOrder = new ArrayList<>();
    private int spriteCount;
    float angle;
    float scale;
    RectF baseRact;
    PointF currentPos;
    private int currentSprite;

    private int textureName = 0;
    private int textureID = 0;
    public boolean drawThis;


    public GEntity(float modelBaseHeight, float modelBaseWidth,
                   int textureAtlasRows, int textureAtlasColumns,
                   PointF pos) {
        this.textureAtlasRows = textureAtlasRows;
        this.textureAtlasColumns = textureAtlasColumns;

        width = modelBaseWidth / 2;
        height = modelBaseHeight / 2;
        this.baseRact = new RectF(-width, height, width, -height);

        // Initial Pos
        currentPos = pos;
        // Initial size
        scale = 1f;
        // Initial angle
        angle = 0f;
        drawThis = true;

    }


    public GEntity() {
        this(100f, 100f, 1, 1, new PointF(50f, 50f));
    }


    public void moveBy(float deltaX, float deltaY) {
        // Update our location.
        currentPos.x += deltaX;
        currentPos.y += deltaY;
    }

    public void placeAt(float x, float y) {
        // Update our location.
        currentPos.x = x;
        currentPos.y = y;
    }

    public void scale(float deltas) {
        scale += deltas;
    }

    public void rotate(float deltaa) {
        angle += deltaa;
    }


    protected void makeSprites() {
        spriteCount = textureAtlasColumns * textureAtlasRows;
        float xOffset = 1 / (float) textureAtlasColumns;
        float yOffset = 1 / (float) textureAtlasRows;
        RectF subTexture = new RectF();
        subTexture.right = xOffset;
        subTexture.bottom = yOffset;
        for (int i = 0; i < textureAtlasRows; i++) {
            for (int j = 0; j < textureAtlasColumns; j++) {
                sprites.add(new RectF(subTexture));
                subTexture.left = subTexture.right;
                subTexture.right += xOffset;
            }
            subTexture.top = subTexture.bottom;
            subTexture.bottom += yOffset;
            subTexture.left = 0;
            subTexture.right = xOffset;
        }
    }

    public void loadTextrue(Bitmap bmp) {
        int id[] = GlRendere.loadTextrue(bmp);
        textureID = id[GlRendere.TEXTURE_SLOT];
        textureName = id[GlRendere.TEXTURE_NAME];
    }

    public int getTextureID() {
        return textureID;
    }

    /**
     * Keep this!! we may change the transformation method in the future
     *
     * @return
     */
    protected float[] getModel() {
        return getTransformedVertices();
    }

    protected float[] getSpriteUvs() {
        Log.d("sprite", sprites.get(currentSprite).toString());
        Log.d("sprite", "" + GraphicsTools.getCornersFromRect(sprites.get(currentSprite)).length);
        Log.d("sprite", Arrays.toString(GraphicsTools.getCornersFromRect(sprites.get(currentSprite))));
        return GraphicsTools.getCornersFromRect(sprites.get(currentSprite));
    }

    public String spritesToString() {
        return GraphicsTools.allVertecisToString(sprites);
    }


    //Flyt ud!!
    private float[] getTransformedVertices() {
        // Start with scaling
        float x1 = baseRact.left * scale;
        float x2 = baseRact.right * scale;
        float y1 = baseRact.bottom * scale;
        float y2 = baseRact.top * scale;

        // We now detach from our Rect because when rotating,
        // we need the seperate points, so we do so in opengl order
        PointF one = new PointF(x1, y2);
        PointF two = new PointF(x1, y1);
        PointF three = new PointF(x2, y1);
        PointF four = new PointF(x2, y2);

        // We create the sin and cos function once,
        // so we do not have calculate them each time.
        float s = (float) Math.sin(angle);
        float c = (float) Math.cos(angle);

        // Then we rotate each point
        one.x = x1 * c - y2 * s;
        one.y = x1 * s + y2 * c;
        two.x = x1 * c - y1 * s;
        two.y = x1 * s + y1 * c;
        three.x = x2 * c - y1 * s;
        three.y = x2 * s + y1 * c;
        four.x = x2 * c - y2 * s;
        four.y = x2 * s + y2 * c;

        // Finally we translate the sprite to its correct position.
        one.x += currentPos.x;
        one.y += currentPos.y;
        two.x += currentPos.x;
        two.y += currentPos.y;
        three.x += currentPos.x;
        three.y += currentPos.y;
        four.x += currentPos.x;
        four.y += currentPos.y;

        // We now return our float array of vertices.
        return new float[]
                {
                        one.x, one.y, 0.0f,
                        two.x, two.y, 0.0f,
                        three.x, three.y, 0.0f,
                        four.x, four.y, 0.0f,
                };
    }


    public int getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(int currentSprite) {
        currentSprite = currentSprite % spriteCount;
        this.currentSprite = currentSprite;
    }

    public void nextSprite() {
        currentSprite++;
        currentSprite = currentSprite % spriteCount;
    }


    @Override
    public int compareTo(GEntity o) {
        return Integer.compare(this.textureID, o.textureID);
    }
}
