package com.gamelogic;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by thor on 11/24/16.
 */


public class Shooter {
    private static final int BASE_SPEED = 20;
    int damage = 10;
    class Shot {
        public Direction direction;
        public Entity shot;
        public int speed;
        public int animationCounter = 0;

        public void move() {
            direction.set(direction.getAngle(), BASE_SPEED);
            shot.moveBy(DataContainer.mapMovement.x, DataContainer.mapMovement.y, 0);
            shot.move(direction);
            shot.getPosition().x += direction.velocity_X;
            shot.getPosition().y += direction.velocity_Y;
        }
    }

    private SpriteEntityFactory shotFactory;
    private ArrayList<Shot> shots;
    private Direction baseDirection;

    public Shooter() {
        shotFactory = new SpriteEntityFactory(R.drawable.beam_green, 100, 60, 3, 1, new PointF(400, 400));
        shots = new ArrayList<>();


    }

    public void shoot(PointF shooterGlobalPos, RectF shooterBaseRect, Direction shooterDirection) {
        Shot s = new Shot();
        s.shot = shotFactory.createEntity();
        s.shot.placeAt(shooterBaseRect.centerX(), shooterBaseRect.centerY());
        s.shot.setPosition(shooterGlobalPos);
        s.direction = new Direction(shooterDirection, 1);
        s.shot.setCurrentSprite(0);
        s.shot.setAngleOffSet(90);
        shots.add(s);
    }

    /**
     * Kan lave optimering ved at genbruge shots!!
     */
    public void update(EnemySpawner enemies) {

        for (Iterator<Shot> it = shots.iterator(); it.hasNext(); ) {
            Shot s = it.next();
            if(s.animationCounter < 5){
                s.animationCounter++;
            }else{
                s.shot.setCurrentSprite(1);
            }
            s.move();

            if (!DataContainer.mapBaseRect.contains(s.shot.getRect())) {
                s.shot.delete();
                it.remove();
                continue;
//                LL(this, "Deleting shot");
            }
            for (Iterator<Enemy> enemyIterator = enemies.getEnemies().iterator(); enemyIterator.hasNext(); ){
                Enemy enemy = enemyIterator.next();
                if(enemy.state.getCurrentState() == enemy.DIYNG){
                    continue;
                }
//                LL(this,"ECheking enemy");

                if (enemy.getEnemyEntity().collision(s.shot.getPosition())){
                    enemy.doDamge(damage);
                    s.shot.delete();
                    it.remove();
                    break;
                }
            }
//            LL(this, "updating  shot list");
        }

    }


}
