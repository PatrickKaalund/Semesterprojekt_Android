package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;


class Enemy extends Creature {
    private static final int NORMAL = 0;
    static final int DIYNG = 2;
    private static final int ATACKING = 3;

    public static final int DYING_STATE_COUNT = 2000;
    public static final int GOT_HIT_STATE_COUNT = 10;
    public static final int ATACKING_STATE_COUNT = 20;

    public static final int NORMAL_ANIMATION_DIV = 3;
    public static final int GOT_HIT_ANIMATION_DIV = 6;
    public static final int DIYNG_ANIMATION_DIV = 4;
    public static final int ATACKING_ANIMATION_DIV = 3;

    public static final int[] NORMAL_ANIMATIONS =  new int[]{4, 5, 6, 7, 8, 9, 10, 11};
    public static final int[] DIYNG_ANIMATIONS =  new int[]{28, 29, 30, 31, 32, 33, 34, 35};
    public static final int[] ATACKING_ANIMATIONS =   new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
    public static final int[] GOT_HIT_ANIMATIONS =   new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21};


    private boolean gotHit = false;
    private int gotHitCounter = 0;
    private EnemySpawner mother;

    public enum EnemyStates_e {
        NORMAL(NORMAL_ANIMATIONS, DYING_STATE_COUNT,NORMAL_ANIMATION_DIV),
        DIYNG(DIYNG_ANIMATIONS, DYING_STATE_COUNT, NORMAL_ANIMATION_DIV),
        ATACKING(NORMAL_ANIMATIONS, DYING_STATE_COUNT, NORMAL_ANIMATION_DIV),
        GOT_HIT(ATACKING_ANIMATIONS, DYING_STATE_COUNT, NORMAL_ANIMATION_DIV);

        private final int[] ANIMATIONS;
        private final int STATE_COUNT;
        private final int ANIMATIONDIV;

        EnemyStates_e(int[] ANIMATIONS, int STATE_COUNT, int ANIMATIONDIV) {
            this.ANIMATIONS = ANIMATIONS;
            this.STATE_COUNT = STATE_COUNT;
            this.ANIMATIONDIV = ANIMATIONDIV;
        }
    }


    public class EnemyStates {
        private int currentState = NORMAL;
        private int stateCounter = 0;
        private int[][] animations = new int[4][0];

        EnemyStates() {
            animations[NORMAL] = new int[]{4, 5, 6, 7, 8, 9, 10, 11};
            animations[DIYNG] = new int[]{28, 29, 30, 31, 32, 33, 34, 35};
            animations[ATACKING] = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
        }

        int[] getAnimations() {
            return animations[currentState];
        }

        void setState(int state) {
            stateCounter = 0;
            currentState = state;
        }


        int getCurrentState() {
            return currentState;
        }

        public int getStateCounter() {
            return stateCounter;
        }
    }

    EnemyStates state;
    private Entity enemy;
    private int damage = 5;

    private Direction direction;
    private int angle;

    Enemy(BackgroundEntity map, Entity enemy, int health, int speed, PointF startLocation) {
        this.mother = mother;

        super.speed = speed;
        super.health = health;
        state = new EnemyStates();
        this.enemy = enemy;

        placeElementFromGlobalPos(map, startLocation);

        this.enemy.setCurrentSprite(4);
        this.enemy.setAngleOffSet(-225);     // pointing right
        this.enemy.setAnimationDivider(3);

        this.enemy.setAnimationOrder(state.getAnimations());
        direction = new Direction();
        this.enemy.setHitBoxSize(50, 50);
    }

    private void placeElementFromGlobalPos(BackgroundEntity map, PointF globalPos) {
        PointF initialPosOnScreen = new PointF();
        initialPosOnScreen.x = globalPos.x - map.screenPos.x + DataContainer.instance.gameContext.getResources().getDisplayMetrics().widthPixels / 2;
        initialPosOnScreen.y = globalPos.y - map.screenPos.y + DataContainer.instance.gameContext.getResources().getDisplayMetrics().heightPixels / 2;
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
            if (gotHitCounter++ > GOT_HIT_STATE_COUNT) {
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
                if (enemy.getCurrentSprite() == 14 && player.getPlayerEntity().collision(enemy.getPosition())) {
                    player.doDamage(damage);
                }
                if (enemy.getCurrentSprite() == 21) {
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
                if (state.stateCounter++ == DYING_STATE_COUNT) {
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

    Entity getEnemyEntity() {
        return enemy;
    }


}