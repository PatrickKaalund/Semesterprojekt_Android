package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.core.Game;
import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;

import java.util.Random;


class Enemy extends Creature {


    public static final int DYING_STATE_COUNT = 2000;
    public static final int GOT_HIT_COUNT = 10;
    public static final int ATACKING_STATE_COUNT = 20;

    public static final int NORMAL_ANIMATION_DIV = 3;
    public static final int GOT_HIT_ANIMATION_DIV = 6;
    public static final int DIYNG_ANIMATION_DIV = 4;
    public static final int ATACKING_ANIMATION_DIV = 3;

    public static final int[] NORMAL_ANIMATIONS = new int[]{4, 5, 6, 7, 8, 9, 10, 11};
    public static final int[] DIYNG_ANIMATIONS = new int[]{28, 29, 30, 31, 32, 33, 34, 35};
    public static final int[] ATACKING_ANIMATIONS = new int[]{12, 13, 14, 15, 16, 17, 18, 19, 20, 21};
    public static final int GOT_HIT_OFFSET = 36;

    private final Game game;
    private boolean randDirection;
    private int randDirCounter = 0;
    private int randDirTime = 100;
    private final EnemySpawner mother;
    Random rand;

    private boolean gotHit = false;
    private int stateCounter = 0;
    private int gotHitCounter = 0;
    private Shooter shooter;

    public enum EnemyStates_e {
        NORMAL(NORMAL_ANIMATIONS, 0, ATACKING_ANIMATION_DIV),
        DYING(DIYNG_ANIMATIONS, DYING_STATE_COUNT, DIYNG_ANIMATION_DIV),
        ATTACKING(ATACKING_ANIMATIONS, ATACKING_STATE_COUNT, NORMAL_ANIMATION_DIV);

        private final int[] ANIMATIONS;
        private final int STATE_COUNT;
        private final int ANIMATIONDIV;

        EnemyStates_e(int[] ANIMATIONS, int STATE_COUNT, int ANIMATIONDIV) {
            this.ANIMATIONS = ANIMATIONS;
            this.STATE_COUNT = STATE_COUNT;
            this.ANIMATIONDIV = ANIMATIONDIV;
        }
    }


    public EnemyStates_e currentState;
    private Entity enemy;
    private int damage = 5;

    private Direction direction;
    private int angle;

    Enemy(EnemySpawner enemySpawner, BackgroundEntity map, Entity enemy, int health, int speed, PointF startLocation, Game game) {
        mother = enemySpawner;
        mother.activeEnemys++;
        super.speed = speed;
        super.health = health;
        currentState = EnemyStates_e.NORMAL;
        this.enemy = enemy;
        rand = new Random();

        placeElementFromGlobalPos(map, startLocation);

        this.enemy.setCurrentSprite(4);
        this.enemy.setAngleOffSet(-225);     // pointing right
        this.enemy.setAnimationDivider(3);

        this.enemy.setAnimationOrder(currentState.ANIMATIONS);
        direction = new Direction();
        this.enemy.setHitBoxSize(100, 100);
        this.game = game;
    }

    private void placeElementFromGlobalPos(BackgroundEntity map, PointF globalPos) {
        PointF initialPosOnScreen = new PointF();
        initialPosOnScreen.x = globalPos.x - map.screenPos.x + DataContainer.instance.gameContext.getResources().getDisplayMetrics().widthPixels / 2;
        initialPosOnScreen.y = globalPos.y - map.screenPos.y + DataContainer.instance.gameContext.getResources().getDisplayMetrics().heightPixels / 2;
        this.enemy.placeAt(initialPosOnScreen.x, initialPosOnScreen.y);
        this.enemy.setPosition(new PointF(globalPos.x, globalPos.y));
    }


//    private void checkInternCol(Enemy e){
//        float deltaT = .top - enemy.getPosition().y;
//        float deltaB = ref.bottom - enemy.getPosition().y;
//        float deltaR = ref.right - enemy.getPosition().x;
//        float deltaL = ref.left - enemy.getPosition().x;
//
//
//
//        if ((deltaT < direction.velocity_Y)) {
//            tblr |= (1 << T_BITPOS);
//            direction.velocity_Y = deltaT;
//        }
//        if ((deltaB > direction.velocity_Y)) {//
//            tblr |= (1 << B_BITPOS);
//            direction.velocity_Y = deltaB;
//        }
//        if ((deltaR < direction.velocity_X)) {//Rigth
//            tblr |= (1 << R_BITPOS);
//            direction.velocity_X = deltaR;
//        }
//        if ((deltaL > direction.velocity_X)) {//LEft
//            tblr |= (1 << L_BITPOS);
//            direction.velocity_X = deltaL;
//        }
//    }

    private void move(Player player, int speed) {

        angle = (int) Math.toDegrees(Math.atan2(player.getPos().y - enemy.getPosition().y, player.getPos().x - enemy.getPosition().x));
        direction.set(angle, speed);
        enemy.moveBy(-DataContainer.instance.mapMovement.x, -DataContainer.instance.mapMovement.y);
        enemy.move(direction);

//        for (Enemy e : mother.getEnemies()) {
//            if(e.equals(this)) continue;
//            if(!enemy.collision(e.getEnemyEntity().getPosition())){
//                enemy.move(direction);
//            }
//        }


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


        if ((currentState == EnemyStates_e.NORMAL) && player.getPlayerEntity().collision(enemy.getPosition())) {
            currentState = EnemyStates_e.ATTACKING;
            stateCounter = 0;
            enemy.setAnimationOrder(currentState.ANIMATIONS);
            enemy.setAnimationDivider(currentState.ANIMATIONDIV);
        }

        if (gotHit) {
            if (gotHitCounter++ > GOT_HIT_COUNT) {
                enemy.setAnimationOffset(0);
                this.direction.baseSpeed = 1;
                enemy.setAnimationDivider(NORMAL_ANIMATION_DIV);
                gotHit = false;
            }
        }

        switch (currentState) {
            case NORMAL:

                move(player, this.speed);
                enemy.drawNextSprite();
                break;
            case ATTACKING:
                move(player, 0);
                enemy.drawNextSprite();
                if (enemy.getCurrentSprite() == 14 && player.getPlayerEntity().collision(enemy.getPosition())) {
                    player.doDamage(damage);
                }
                if (enemy.getCurrentSprite() == 21) {
                    if (player.getPlayerEntity().collision(enemy.getPosition())) {
                        currentState = EnemyStates_e.ATTACKING;
                        stateCounter = 0;
                    } else {
                        currentState = EnemyStates_e.NORMAL;
                        enemy.setAnimationOrder(currentState.ANIMATIONS);
                        enemy.setAnimationDivider(currentState.ANIMATIONDIV);
                    }
                }


                break;
            case DYING:

                move(player, 0);

                if (enemy.getCurrentSprite() == 35) {
                    enemy.setCurrentSprite(35);
                } else {
                    enemy.drawNextSprite();
                }
                if (stateCounter++ == currentState.STATE_COUNT) {
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

    public boolean doDamage(int damage) {

        if (!gotHit) {
            super.health -= damage;
            if (super.health < 0) {
                mother.activeEnemys--;
                currentState = EnemyStates_e.DYING;
                enemy.setAnimationDivider(currentState.ANIMATIONDIV);
                enemy.setAnimationOrder(currentState.ANIMATIONS);
                game.getPlayer().getShooter().incrementKills(1);
                return gotHit = true;
            } else {
                gotHitCounter = 0;
                enemy.setAnimationOffset(GOT_HIT_OFFSET);
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