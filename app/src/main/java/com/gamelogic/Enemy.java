package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.provider.ContactsContract;
import android.util.Log;

import com.graphics.Direction;
import com.graphics.Entity;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Enemy extends Creature {
    public static final int NORMAL = 0;
    public static final int DIYNG = 2;
    public static final int ATACKING = 3;

    public static final int DIYNG_STATE_COUNTER = 2000;
    public static final int GOT_HIT_STATE_COUNTER = 10;
    public static final int ATACKING_STATE_COUNTER = 20;

    public static final int NORMAL_ANIMATION_DIV = 3;
    public static final int GOT_HIT_ANIMATION_DIV = 6;
    public static final int DIYNG_ANIMATION_DIV = 4;
    public static final int ATACKING_ANIMATION_DIV = 3;
    private boolean gotHit = false;
    private int gotHitCounter = 0;
    private EnemySpawner mother;


    public class EnemyStates {
        private int currentState = NORMAL;
        private int stateCounter = 0;
        private int[][] animations = new int[4][0];

        public EnemyStates() {
            animations[NORMAL] = new int[]{4, 5, 6, 7, 8, 9, 10, 11};
            animations[DIYNG] = new int[]{28, 29, 30, 31, 32, 33, 34, 35};
            animations[ATACKING] = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        }

        public int[] getAnimations() {
            return animations[currentState];
        }

        public void setState(int state) {
            stateCounter = 0;
            currentState = state;
        }


        public int getCurrentState() {
            return currentState;
        }

        public int getStateCounter() {
            return stateCounter;
        }
    }

    public EnemyStates state;
    private Entity enemy;
    private int damege = 5;

    Direction direction;
    int angle;

    public Enemy(Entity enemy, int health, int speed, PointF startLocation) {


        super.speed = speed;
        super.health = health;
        state = new EnemyStates();
        this.enemy = enemy;

        placeElementFromGlobalPos(startLocation);

        this.enemy.setCurrentSprite(4);
        this.enemy.setAngleOffSet(-225);     // pointing right
        this.enemy.setAnimationDivider(3);
        this.enemy.setAnimationOrder(state.getAnimations());
        direction = new Direction();
        this.enemy.setHitBoxSize(50,50);
    }

    private void placeElementFromGlobalPos(PointF globalPos) {
        PointF initialPosOnScreen = new PointF();
        initialPosOnScreen.x = globalPos.x - DataContainer.instance.player.getPos().x + DataContainer.instance.gameContext.getResources().getDisplayMetrics().widthPixels / 2;
        initialPosOnScreen.y = globalPos.y - DataContainer.instance.player.getPos().y + DataContainer.instance.gameContext.getResources().getDisplayMetrics().heightPixels / 2;
        this.enemy.placeAt(initialPosOnScreen.x, initialPosOnScreen.y);
        this.enemy.setPosition(new PointF(globalPos.x, globalPos.y));
    }

    private void move(Player player, int speed) {
        angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
        direction.set(angle, speed);
        enemy.moveBy(-DataContainer.instance.mapMovement.x, -DataContainer.instance.mapMovement.y);
        enemy.move(direction);
    }


    public boolean update(Player player) {

//        Log.e(this.getClass().getCanonicalName(), "Mapmovement: " + DataContainer.mapMovement.x + ", " + DataContainer.mapMovement.y);

//        Log.e(this.getClass().getCanonicalName(), "Player pos x: " + player.getPos().x + " y " + player.getPos().y);
//        Log.e(this.getClass().getCanonicalName(), "Enemy pos x: " + enemy.getPosition().x + " y " + enemy.getPosition().y);
//        Log.e(this.getClass().getCanonicalName(), "Enemy rect " + enemy.getRect().toString());
//
//        angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
////                Log.e(this.getClass().getCanonicalName(), "normal angle: "+ angle);
//
//        direction.set(angle, this.speed);
//        // Adjust for map movement
//        move(direction);
//        enemy.drawNextSprite();


        if (state.getCurrentState() == NORMAL && player.getPlayerEntity().collision(enemy.getPosition())) {
            state.setState(ATACKING);
            enemy.setAnimationOrder(state.getAnimations());
            enemy.setAnimationDivider(ATACKING_ANIMATION_DIV);
        }

        if (gotHit) {
            if (gotHitCounter++ > GOT_HIT_STATE_COUNTER) {
                enemy.setAnimationOffset(0);
                this.direction.baseSpeed = 1;
                enemy.setAnimationDivider(NORMAL_ANIMATION_DIV);
                gotHit = false;
            }
        }

        switch (state.getCurrentState()) {
            case NORMAL:

                move(player, this.speed);
                enemy.drawNextSprite();
                break;
            case ATACKING:
                move(player, 0);
                enemy.drawNextSprite();
                if (state.stateCounter++ == ATACKING_STATE_COUNTER / 2) {
                    player.doDamage(damege);
                } else if (state.stateCounter == ATACKING_STATE_COUNTER) {
                    if (player.getPlayerEntity().collision(enemy.getPosition())) {
                        state.setState(ATACKING);
                    } else {
                        state.setState(NORMAL);
                        enemy.setAnimationOrder(state.getAnimations());
                        enemy.setAnimationDivider(NORMAL_ANIMATION_DIV);
                    }

                }

                break;
            case DIYNG:

                move(player, 0);

                if (enemy.getCurrentSprite() == 35) {
                    enemy.setCurrentSprite(35);
                } else {
                    enemy.drawNextSprite();
                }
                if (state.stateCounter++ == DIYNG_STATE_COUNTER) {
                    enemy.delete();
                    return false;
                }

                break;
            default:
                Log.e(this.getClass().getCanonicalName(), "DEFAULTED IN UPDATE");
                break;
        }

        return true;
    }

    public boolean doDamage(int damege) {

        if (!gotHit) {
            super.health -= damege;
            if (super.health < 0) {
                state.setState(DIYNG);
                enemy.setAnimationDivider(DIYNG_ANIMATION_DIV);
                enemy.setAnimationOrder(state.getAnimations());
            } else {
                gotHitCounter = 0;
                enemy.setAnimationOffset(36);
                enemy.setAnimationDivider(GOT_HIT_ANIMATION_DIV);
                if (state.currentState != ATACKING) {
                    this.direction.baseSpeed = 3;
                }
                return gotHit = true;
            }
        }
        return false;
    }

    public RectF getRect() {
        return this.enemy.getRect();
    }

    public Entity getEnemyEntity() {
        return enemy;
    }


}