package com.gamelogic;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.gamelogic.DataContainer.gameContext;
import static com.graphics.GraphicsTools.LL;

/**
 * Created by thor on 11/24/16.
 */


public class Shooter {
    private static final int BASE_SPEED = 20;
    int damage = 10;
    private SharedPreferences preferences;

    class Shot {
        public Direction direction;
        public Entity shot;
        public int speed;
        public int animationCounter = 0;

        public void move() {
            direction.set(direction.getAngle(), BASE_SPEED);
            shot.placeAt(500, 500);
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
        shotFactory = new SpriteEntityFactory(R.drawable.bullets, 20, 30, 1, 1, new PointF(400, 400));
        shots = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(gameContext);
    }

    public void shoot(PointF shooterGlobalPos, RectF shooterBaseRect, Direction shooterDirection, Player.weaponSelection_e currentWeapon) {
        LL(this, "crating a shot " +

                " shooterGlobalPos " + shooterGlobalPos +
                " shooterBaseRect " + shooterBaseRect.toString() +
                " shooterDirection " + shooterDirection.toString()


        );

        switch (currentWeapon) {

            case GUN:
                if (preferences.getBoolean("sound", true)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(gameContext, R.raw.gun);
                    mediaPlayer.start();
                }

                break;
            case SHOTGUN:
                if (preferences.getBoolean("sound", true)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(gameContext, R.raw.shotgun);
                    mediaPlayer.start();
                }

                break;
            case AK47:
                if (preferences.getBoolean("sound", true)) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(gameContext, R.raw.ak);
                    mediaPlayer.start();
                }

                break;
            default:
                Log.e("SHOOTER", "DEFULTED IN SHOOTER::shoot: switch (currentWeapon)");
                break;
        }

        Shot s = new Shot();
        s.shot = shotFactory.createEntity();
        s.shot.placeAt(shooterBaseRect.centerX(), shooterBaseRect.centerY());
        s.shot.setPosition(shooterGlobalPos);
        s.direction = new Direction(shooterDirection, 1);
        s.shot.setCurrentSprite(0);
        s.shot.setAngleOffSet(0);
        shots.add(s);
    }

    /**
     * Kan lave optimering ved at genbruge shots!!
     */
    public void update(EnemySpawner enemies) {

        for (Iterator<Shot> it = shots.iterator(); it.hasNext(); ) {
            Shot s = it.next();
            LL(this, "Shoter update at: " + Arrays.toString(GraphicsTools.getCornersFromRect(s.shot.getRect())));

            if (s.animationCounter < 5) {
                s.animationCounter++;
            } else {
                s.shot.setCurrentSprite(1);
            }
            s.move();

            if (!DataContainer.mapBaseRect.contains(s.shot.getRect())) {
                s.shot.delete();
                it.remove();
                continue;
//                LL(this, "Deleting shot");
            }
            for (Enemy enemy : enemies.getEnemies()) {
                if (enemy.state.getCurrentState() == Enemy.DIYNG) {
                    continue;
                }
//                LL(this,"ECheking enemy");

                if (enemy.getEnemyEntity().collision(s.shot.getPosition())) {
                    enemy.doDamage(damage);
                    s.shot.delete();
                    it.remove();
                    break;
                }
            }
//            LL(this, "updating  shot list");
        }

    }


}
