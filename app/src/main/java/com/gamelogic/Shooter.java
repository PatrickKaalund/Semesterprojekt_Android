package com.gamelogic;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.Log;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Iterator;

import static com.graphics.GraphicsTools.LL;


class Shooter {
    private SharedPreferences preferences;
    private AudioPlayer audioPlayer;
    private WeaponsHandler weaponsHandler;

    private class Shot {
        public Direction direction;
        Entity shot;
        int animationCounter = 0;
        int shotSpeed;

        void move() {
            direction.set(direction.getAngle(), shotSpeed);
            shot.moveBy(-DataContainer.instance.mapMovement.x, -DataContainer.instance.mapMovement.y, 0);
            shot.move(direction);
//            shot.getPosition().x += direction.velocity_X;
//            shot.getPosition().y += direction.velocity_Y;
        }
    }

    private SpriteEntityFactory shotFactory;
    private ArrayList<Shot> shots;

    Shooter(WeaponsHandler weaponsHandler) {
        this.weaponsHandler = weaponsHandler;

        shotFactory = new SpriteEntityFactory(R.drawable.bullets_scaled, 20, 30, 1, 2, new PointF(400, 400));
        shots = new ArrayList<>();

        preferences = PreferenceManager.getDefaultSharedPreferences(DataContainer.instance.gameContext);

        audioPlayer = new AudioPlayer(DataContainer.instance.gameContext);
    }

    void shoot(PointF shooterGlobalPos, RectF shooterBaseRect, Direction shooterDirection, WeaponsHandler.WeaponList_e currentWeapon) {


        if (weaponsHandler.getCurrentAmmoAmount() > 0) {
            LL(this, "crating a shot " +

                    " shooterGlobalPos " + shooterGlobalPos +
                    " shooterBaseRect " + shooterBaseRect.toString() +
                    " shooterDirection " + shooterDirection.toString()
            );

            if (preferences.getBoolean("sound", true)) {
                switch (currentWeapon) {
                    case GUN:
                        audioPlayer.playAudioFromRaw(R.raw.gun);
                        break;
                    case SHOTGUN:
                        audioPlayer.playAudioFromRaw(R.raw.shotgun);
                        break;
                    case AK47:
                        audioPlayer.playAudioFromRaw(R.raw.ak);
                        break;
                    default:
                        Log.e("SHOOTER", "DEFAULTED IN SHOOTER::shoot: switch (currentWeapon)");
                        break;
                }
            }

            Shot s = new Shot();
            s.shot = shotFactory.createEntity();
            s.shot.placeAt(shooterBaseRect.centerX(), shooterBaseRect.centerY());
            s.shot.setPosition(shooterGlobalPos);
            s.direction = new Direction(shooterDirection, 1);
            s.shot.setCurrentSprite(0);
            s.shot.setAngleOffSet(0);
            s.shotSpeed = weaponsHandler.getCurrentWeapon().SHOT_SPEED;
            shots.add(s);
            weaponsHandler.setCurrentAmmoAmount(weaponsHandler.getCurrentAmmoAmount() - 1);
            Log.d("SHOOTER", "AMMO LEFT: " + weaponsHandler.getCurrentAmmoAmount());
        } else {
            audioPlayer.playAudioFromRaw(R.raw.dry_fire);
            Log.d("SHOOTER", "OUT OF AMMO");
        }
    }

    /**
     * Kan lave optimering ved at genbruge shots!!
     */
    public void update(EnemySpawner enemies, int damage) {

        for (Iterator<Shot> it = shots.iterator(); it.hasNext(); ) {
            Shot s = it.next();
//            LL(this, "Shoter update at: " + Arrays.toString(GraphicsTools.getCornersFromRect(s.shot.getRect())));
//

            if (s.animationCounter < 5) {
                s.animationCounter++;
            } else {
                s.shot.setCurrentSprite(1);
            }
            s.move();

            if (s.shot.getPosition().x < 0 ||
                    s.shot.getPosition().x > DataContainer.instance.mapGlobalSize.x ||
                    s.shot.getPosition().y < 0 ||
                    s.shot.getPosition().x > DataContainer.instance.mapGlobalSize.y) {
                s.shot.delete();
                it.remove();
                LL(this, "Deleting shot");
                continue;
            }
            for (Enemy enemy : enemies.getEnemies()) {
                if (enemy.currentState == Enemy.EnemyStates_e.DYING) {
                    continue;
                }
//                LL(this,"ECheking enemy");

                if (enemy.getEnemyEntity().collision(s.shot.getPosition())) {
                    if(enemy.doDamage(damage)){
                        s.shot.delete();
                        LL(this, "Deleting shot");
                        it.remove();
                        break;
                    }
                }
            }
//            LL(this, "updating  shot list");
        }
    }
}
