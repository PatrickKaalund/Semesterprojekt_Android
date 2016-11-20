package com.graphics;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import java.util.Arrays;

import static com.graphics.GraphicsTools.LL;
import static com.graphics.GraphicsTools.rectToString;


/**
 * Created by thor on 10/22/16.
 */

class SpriteEntity extends GraphicEntity implements Entity {


    int[] animationOrder = new int[0];
    float angleOffSet;
    float scale;
    private int currentSprite;
    private int drawOrderIndex = 0;
    private int animationDivider = 1;
    private int animationCounter = 0;
    float[] modelPoints;


    private PointF position;

    private SpriteEntityFactory mother;
    protected int index;


    public SpriteEntity(float modelBaseHeight, float modelBaseWidth,
                        PointF pos, SpriteEntityFactory mother, int index) {
        float width = modelBaseWidth / 2;
        float height = modelBaseHeight / 2;
        PointF motherPos = mother.getPos();
        this.baseRact = new RectF(motherPos.x - width, motherPos.y + height, motherPos.x + width, motherPos.y - height);
        Log.d("SpriteEntity", "BaseRect: " + rectToString(baseRact));
        this.mother = mother;
        this.index = index;
        position = pos;
        // Initial size
        scale = 1f;
        // Initial angleOffSet
        angleOffSet = 0f;
        mustDrawThis(true);
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);

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


    protected float[] getModel() {
//        drawNextSprite();
        float[] modelPointWithz = {
                modelPoints[0], modelPoints[1], 0f,
                modelPoints[2], modelPoints[3], 0f,
                modelPoints[4], modelPoints[5], 0f,
                modelPoints[6], modelPoints[7], 0f,
        };
        return modelPointWithz;
    }


    public Direction move(Direction direction) {
        Matrix transformationMatrix = new Matrix();

        switch (direction.lock) {
            case Direction.X:
                transformationMatrix.setTranslate(0, direction.calcVelocity_Y());
                break;
            case Direction.Y:
                transformationMatrix.setTranslate(direction.calcVelocity_X(), 0);
                break;
            case Direction.UNLOCK:
                transformationMatrix.setTranslate(direction.calcVelocity_X(), direction.calcVelocity_Y());
                break;
            case Direction.ALL:
                break;
        }
//        Log.w("SpriteEntety", "baseRact before: " + rectToString(baseRact));
        transformationMatrix.mapRect(baseRact);
//        Log.w("SpriteEntety", "baseRact after: " + rectToString(baseRact));
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(direction.getAngle() + angleOffSet, baseRact.centerX(), baseRact.centerY());
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        rotationMatrix.mapPoints(modelPoints);
        return direction;
    }

//    public void setLock(LockDirection lockDirection) {
//        this.lockDirection = lockDirection;
//    }


    public void moveBy(float deltaX, float deltaY) {

//        Log.d("SpriteEntity", "Rectangle before: " + baseRact.toString());

        baseRact.set(baseRact.left + deltaX, baseRact.top + deltaY, baseRact.right + deltaX, baseRact.bottom + deltaY);

//        Log.d("SpriteEntity", "Rectangle after: " + baseRact.toString());
    }

    public void placeAt(float x, float y) {

        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        LL(this,"place at: "+ Arrays.toString(modelPoints));
        // Update our location.
        baseRact.set(x - baseRact.width() / 2, y - baseRact.height() / 2, x + baseRact.width() / 2, y + baseRact.height() / 2);
        position.set(x,y);
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        LL(this,"place at: "+ Arrays.toString(modelPoints));


    }

    public void scale(float deltas) {
        scale += deltas;
    }

    public void rotate(float deltaa) {
        angleOffSet += deltaa;
    }

    public void setAngleOffSet(float angleOffSet) {
        this.angleOffSet = angleOffSet;
    }

    public int getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(int currentSprite) {
        this.currentSprite = currentSprite;
    }

    public RectF getRect() {
        return this.baseRact;
    }

    public void drawNextSprite() {
        if (++this.animationCounter == this.animationDivider) {
            animationCounter = 0;
            if (animationOrder.length == 0) {
                currentSprite++;
                currentSprite = currentSprite % mother.spriteCount;
            } else {
                drawOrderIndex++;
                drawOrderIndex = drawOrderIndex % animationOrder.length;
                currentSprite = animationOrder[drawOrderIndex];
            }
        }
    }

    public void setAnimationOrder(int[] animationOrder) {
        this.animationOrder = animationOrder;
    }

    public void setAnimationDivider(int animationDivider) {
        this.animationDivider = animationDivider;
    }

    public PointF getPosition() {
        return position;
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
//    protected float[] getModel() {
//
//        return getTransformedVertices();
//    }
//    private float[] getTransformedVertices() {
//        // Start with scaling
//        float x1 = baseRact.left * scale;
//        float x2 = baseRact.right * scale;
//        float y1 = baseRact.bottom * scale;
//        float y2 = baseRact.top * scale;
//
//        // We now detach from our Rect because when rotating,
//        // we need the seperate points, so we do so in opengl order
//        PointF one = new PointF(x1, y2);
//        PointF two = new PointF(x1, y1);
//        PointF three = new PointF(x2, y1);
//        PointF four = new PointF(x2, y2);
//
//        // We create the sin and cos function once,
//        // so we do not have calculate them each time.
//        float s = (float) Math.sin(angleOffSet);
//        float c = (float) Math.cos(angleOffSet);
//
//        // Then we rotate each point
//        one.x = x1 * c - y2 * s;
//        one.y = x1 * s + y2 * c;
//        two.x = x1 * c - y1 * s;
//        two.y = x1 * s + y1 * c;
//        three.x = x2 * c - y1 * s;
//        three.y = x2 * s + y1 * c;
//        four.x = x2 * c - y2 * s;
//        four.y = x2 * s + y2 * c;
//
//        // Finally we translate the sprite to its correct position.
//        one.x += currentPos.x;
//        one.y += currentPos.y;
//        two.x += currentPos.x;
//        two.y += currentPos.y;
//        three.x += currentPos.x;
//        three.y += currentPos.y;
//        four.x += currentPos.x;
//        four.y += currentPos.y;
//
//        // We now return our float array of vertices.
//        return new float[]
//                {
//                        one.x, one.y, 0.0f,
//                        two.x, two.y, 0.0f,
//                        three.x, three.y, 0.0f,
//                        four.x, four.y, 0.0f,
//                };
//    }


}
