package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.graphics.Direction;
import com.graphics.Entity;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Enemy extends Creature {
    public static final int NORMAL = 0;
    public static final int GOT_HIT = 1;
    public static final int DIYNG = 2;
    public static final int ATACKING = 3;

    public static final int DIYNG_STATE_COUNTER = 2000;
    public static final int GOT_HIT_STATE_COUNTER = 10;
    public static final int ATACKING_STATE_COUNTER = 20;

    public static final int NORMAL_ANIMATION_DIV = 3;
    public static final int GOT_HIT_ANIMATION_DIV = 200;
    public static final int DIYNG_ANIMATION_DIV = 5;
    public static final int ATACKING_ANIMATION_DIV = 20;
    private EnemySpawner mother;


    public class EnemyStates {
        private int currentState = NORMAL;
        private int stateCounter = 0;
        private int[][] animations = new int[4][0];

        public EnemyStates() {
            animations[NORMAL] = new int[]{4, 5, 6, 7, 8, 9, 10, 11};
            animations[GOT_HIT] = new int[]{0, 1, 2, 3};
            animations[DIYNG] = new int[]{28, 29, 30, 31, 32, 33, 34, 35};
            animations[ATACKING] = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};
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
        this.enemy.placeAt(startLocation.x, startLocation.y);
        this.enemy.setCurrentSprite(4);
        this.enemy.setAngleOffSet(-135);     // pointing right
        this.enemy.setAnimationDivider(3);
        this.enemy.setAnimationOrder(state.getAnimations());

        direction = new Direction();

    }

    private void move(Direction direction) {
        enemy.moveBy(-DataContainer.mapMovement.x, -DataContainer.mapMovement.y, 0);
        enemy.move(direction);
    }


    public boolean update(Player player) {
        Log.e(this.getClass().getCanonicalName(), "Player pos y: " + player.getPos().y + " x " + player.getPos().x);
        Log.e(this.getClass().getCanonicalName(), "Enemy pos y: " + enemy.getPosition().y + " x " + enemy.getPosition().x);
        Log.e(this.getClass().getCanonicalName(), "Enemy rect " + enemy.getRect().toString());

        angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
//                Log.e(this.getClass().getCanonicalName(), "normal angle: "+ angle);

        direction.set(angle, this.speed);
        // Adjust for map movement
        move(direction);
        enemy.drawNextSprite();

//
//        if (state.getCurrentState() == NORMAL && player.getPlayerEntity().collision(enemy.getPosition())) {
//            state.setState(ATACKING);
//            enemy.setAnimationOrder(state.getAnimations());
//        }
//
//
//        switch (state.getCurrentState()) {
//            case NORMAL:
//                angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
////                Log.e(this.getClass().getCanonicalName(), "normal angle: "+ angle);
//
//                direction.set(angle, this.speed);
//                // Adjust for map movement
//                move(direction);
//                enemy.drawNextSprite();
//                break;
//            case ATACKING:
//                angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
//
//                direction.set(angle, 0);
//                move(direction);
//
//                enemy.drawNextSprite();
//                if (state.stateCounter++ == ATACKING_STATE_COUNTER / 2) {
//                    player.doDamage(damege);
//                } else if (state.stateCounter == ATACKING_STATE_COUNTER) {
//                    state.setState(NORMAL);
//                    enemy.setAnimationOrder(state.getAnimations());
//                    enemy.setAnimationDivider(NORMAL_ANIMATION_DIV);
//                }
//
//                break;
//            case DIYNG:
//                angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
//
//                direction.set(angle, 0);
//                move(direction);
//
//                if (enemy.getCurrentSprite() == 35) {
//                    enemy.setCurrentSprite(35);
//                } else {
//                    enemy.drawNextSprite();
//                }
//                if (state.stateCounter++ == DIYNG_STATE_COUNTER) {
//                    enemy.delete();
//                    return false;
//                }
//
//                break;
//            case GOT_HIT:
//                angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
//
////                Log.e(this.getClass().getCanonicalName(), "Got hit angle: "+ angle);
//                direction.set(angle, 0);
//                move(direction);
//
//                enemy.drawNextSprite();
//                if (state.stateCounter++ == GOT_HIT_STATE_COUNTER) {
//                    state.setState(NORMAL);
//                    enemy.setAnimationOrder(state.getAnimations());
//                }
////                Log.e(this.getClass().getCanonicalName(), "got hit current sprite: "+ enemy.getCurrentSprite() );
//
//
//                break;
//            default:
//                Log.e(this.getClass().getCanonicalName(), "DEFAULTED IN UPDATE");
//                break;
//        }

        return true;
    }

    public void doDamage(int damege) {
        super.health -= damege;
        if (super.health < 0) {
            state.currentState = DIYNG;
            enemy.setAnimationDivider(DIYNG_ANIMATION_DIV);
            enemy.setAnimationOrder(state.getAnimations());
        } else {
            state.currentState = GOT_HIT;
            enemy.setAnimationDivider(GOT_HIT_STATE_COUNTER);
            enemy.setAnimationOrder(state.getAnimations());
        }

    }

    public RectF getRect() {
        return this.enemy.getRect();
    }

    public Entity getEnemyEntity() {
        return enemy;
    }


}