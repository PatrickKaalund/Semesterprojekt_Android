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
    private int animationOffset = 0;
    float[] modelPoints;
    protected float width;
    protected float height;
    boolean isHit = false;
    private RectF baseRact;

    private PointF position;

    private SpriteEntityFactory mother;
    protected int index;


    public SpriteEntity(float modelBaseHeight, float modelBaseWidth,
                        PointF pos, SpriteEntityFactory mother) {
        width = modelBaseWidth / 2;
        height = modelBaseHeight / 2;
        PointF motherPos = mother.getPos();
        this.baseRact = new RectF(motherPos.x - width, motherPos.y + height, motherPos.x + width, motherPos.y - height);
//        Log.d("SpriteEntity", "BaseRect: " + rectToString(baseRact));
        this.mother = mother;
//        this.index = index;
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
//        LL(this, "getModel at: " + Arrays.toString(modelPoints));
//        drawNextSprite();
        float[] modelPointWithz = {
                modelPoints[0], modelPoints[1], 0f,
                modelPoints[2], modelPoints[3], 0f,
                modelPoints[4], modelPoints[5], 0f,
                modelPoints[6], modelPoints[7], 0f,
        };
        return modelPointWithz;
    }


    public void setHitBoxSize(int width, int height) {
        this.width = width;
        this.height = height;
    }


    public Direction move(Direction direction) {

        this.position.x += direction.velocity_X;
        this.position.y += direction.velocity_Y;

        Matrix transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(direction.velocity_X, direction.velocity_Y);

//        Log.w("SpriteEntety", "baseRact before: " + rectToString(baseRact));
        transformationMatrix.mapRect(baseRact);
//        Log.w("SpriteEntety", "baseRact after: " + rectToString(baseRact));
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(direction.getAngle() + angleOffSet, baseRact.centerX(), baseRact.centerY());
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        rotationMatrix.mapPoints(modelPoints);
        return direction;

    }


    public Direction moveBy(Direction direction) {

        Matrix transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(direction.velocity_X, direction.velocity_Y);

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


    public void moveBy(float deltaX, float deltaY, float angle) {
//        this.position.x += deltaX;
//        this.position.y += deltaY;

        Matrix transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(deltaX, deltaY);
        transformationMatrix.mapRect(baseRact);
//        Log.w("SpriteEntety", "baseRact after: " + rectToString(baseRact));
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(angle + angleOffSet, baseRact.centerX(), baseRact.centerY());
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        rotationMatrix.mapPoints(modelPoints);

//        Log.d("SpriteEntity", "Rectangle before: " + baseRact.toString());

//        baseRact.set(baseRact.left + deltaX, baseRact.top + deltaY, baseRact.right + deltaX, baseRact.bottom + deltaY);

//        Log.d("SpriteEntity", "Rectangle after: " + baseRact.toString());
    }

    public void moveBy(float deltaX, float deltaY) {
        moveBy(deltaX, deltaY, 0);
    }

    public void setAngle(float angle) {
        Matrix rotationMatrix = new Matrix();
        rotationMatrix.setRotate(angle, baseRact.centerX(), baseRact.centerY());
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
        rotationMatrix.mapPoints(modelPoints);
    }

    public void placeAt(float x, float y) {

//        LL(this, "place at: " + Arrays.toString(modelPoints));
        // Update our location.
        baseRact.set(x - baseRact.width() / 2, y - baseRact.height() / 2, x + baseRact.width() / 2, y + baseRact.height() / 2);
        position.set(x, y);
        modelPoints = GraphicsTools.getCornersFromRect(baseRact);
//        LL(this, "place at: " + Arrays.toString(modelPoints));


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
        if (++this.animationCounter > this.animationDivider) {
            animationCounter = 0;
            if (animationOrder.length == 0) {
                currentSprite++;
                currentSprite = currentSprite % mother.spriteCount;
            } else {
                drawOrderIndex++;
                drawOrderIndex = drawOrderIndex % animationOrder.length;
                currentSprite = animationOrder[drawOrderIndex] + animationOffset;
            }
        }
    }

    public void setAnimationOrder(int[] animationOrder) {
        animationCounter = 0;
        drawOrderIndex = 0;
        this.animationOrder = animationOrder;
        setCurrentSprite(this.animationOrder[0]);
    }

    public void setAnimationDivider(int animationDivider) {
        this.animationDivider = animationDivider;
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF newPos) {
        this.position.set(newPos.x, newPos.y);
    }

//    public int getIndex() {
//        return index;
//    }

    protected float[] getUvs() {
//        Log.d("sprite", mother.sprites.get(currentSprite).toString());
//        Log.d("sprite", "" + GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite)).length);
//        Log.d("sprite", Arrays.toString(GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite))));
        return GraphicsTools.getCornersFromRect(mother.sprites.get(currentSprite));
    }


    public boolean collision(float x, float y) {
        if (isHit) {
            return false;
        }
//        LL(this, "in x "+x+" in y "+y + " ---->"+ '\n'+
//                " baseRact.left+position.x "+ (position.x-width) +'\n'+
//                " baseRact.right+position.x "+(position.x+width)+'\n'+
//                " baseRact.top+position.y "+(position.y-height) +'\n'+
//                " baseRact.bottom+position.y " +(position.y+height)+'\n'
//        );
//
//        LL(this, "---->"+'\n'+
//                " baseRact.left "+ baseRact.left+'\n'+
//                " baseRact.right "+baseRact.right+'\n'+
//                " baseRact.top "+ baseRact.top +'\n'+
//                " baseRact.bottom " +baseRact.bottom+'\n'
//        );
//
//        LL(this, "----> positionx "+position.x+"positiony "+position.y
//        );


        return (x >= (position.x - width) && x < (position.x + width)
                && y >= (position.y - height) && y < (position.y + height));

    }

    public boolean collision(PointF pos) {
        return collision(pos.x, pos.y);
    }

    public boolean collisionBoarder(Entity entity) {
        SpriteEntity in = (SpriteEntity) entity;
        return false;// (baseRact.contains(in.getRect()));
//        // check for empty first
//        return // now check for containment
//                ((position.x - width) <= (in.position.x - in.width) &&
//                        (position.y - height) <= (in.position.y - in.height) &&
//                        (position.x + width) >= (in.position.x + in.width) &&
//                        (position.y + height) >= (in.position.y + in.height));
//
//        return this.left < this.right && this.top < this.bottom
//                // now check for containment
//                && left <= r.left && top <= r.top
//                && right >= r.right && bottom >= r.bottom;
    }


    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public void delete() {
        mother.removeEntity(this);
    }

    public void setAnimationOffset(int animationOffset) {
        this.animationOffset = animationOffset;
    }
}
