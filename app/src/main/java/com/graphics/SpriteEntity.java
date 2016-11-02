package com.graphics;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;

import static com.graphics.GraphicsTools.rectToString;


/**
 * Created by thor on 10/22/16.
 */

class SpriteEntity extends GraphicEntity implements Entity {


    ArrayList<Integer> drawOrder = new ArrayList<>();
    float angle;
    float scale;
    PointF currentPos;
    private int currentSprite;
    private int drawOrderIndex = 0;


    private SpriteEntityFactory mother;
    protected int index;


    public SpriteEntity(float modelBaseHeight, float modelBaseWidth,
                        PointF pos, SpriteEntityFactory mother, int index) {
        float width = modelBaseWidth / 2;
        float height = modelBaseHeight / 2;
        this.baseRact = new RectF(-width, height, width, -height);
        Log.d("SpriteEntity", "BaseRect: " + rectToString(baseRact));
        this.mother = mother;
        this.index = index;


        // Initial Pos
        currentPos = pos;
        // Initial size
        scale = 1f;
        // Initial angle
        angle = 0f;
        mustDrawThis(true);

    }

    public void mustDrawThis(boolean draw) {
        this.drawThis = draw;
        mother.entityDrawCount += draw ? 1 : -1;
    }

    public boolean mustDrawThis() {
        return drawThis;
    }

//    public SpriteEntity(float height) {
//        this(100f, 100f, 1, 1, new PointF(50f, 50f));
//
//    }


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

    public void setAngle(float angle) {
        this.angle = angle;
    }


    public int getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(int currentSprite) {
        this.currentSprite = currentSprite;
    }

    public void drawNextSprite() {
        if (drawOrder.isEmpty()) {
            currentSprite++;
            currentSprite = currentSprite % mother.spriteCount;
        } else {
            drawOrderIndex++;
            drawOrderIndex = drawOrderIndex % drawOrder.size();
            currentSprite = drawOrder.get(drawOrderIndex);
        }
    }


    public void setDrawOrder(ArrayList<Integer> drawOrder) {
        this.drawOrder = drawOrder;
    }



    public int getIndex() {
        return index;
    }

    protected float[] getUvs() {
//        Log.d("sprite", mother.sprites.get(currentSprite).toString());
//        Log.d("sprite", "" + GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite)).length);
//        Log.d("sprite", Arrays.toString(GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite))));
        return GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite));
    }

    /**
     * Keep this!! we may change the transformation method in the future
     *
     * @return
     */
    protected float[] getModel() {

        return getTransformedVertices();
    }

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


}
