package com.gamelogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.GraphicsTools;
import com.graphics.SpriteEntityFactory;

import java.util.ArrayList;
import java.util.Iterator;

import static com.graphics.GraphicsTools.LL;

/**
 * Created by thor on 11/24/16.
 */


public class Shooter {
    private static final int BASE_SPEED = 20;
    private final Player player;
    private SharedPreferences preferences;
    private Context context;

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

    private SpriteEntityFactory shootFactory;
    private ArrayList<Shot> shots;
    private Direction baseDirection;

    public Shooter(Player player, Context context) {
        this.player = player;
        this.context = context;
        shootFactory = new SpriteEntityFactory(R.drawable.bullets, 30, 40, 1, 2, new PointF(400, 400));

        shots = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void shoot(PointF shooterGlobalPos, RectF shooterBaseRect, Direction shooterDirection) {
        if (player.getCurrentWeapon() == 0) {
            if (preferences.getBoolean("sound", true)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.gun);
                mediaPlayer.start();
            }
        } else if (player.getCurrentWeapon() == 1) {
            if (preferences.getBoolean("sound", true)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.shotgun);
                mediaPlayer.start();
            }
        } else if (player.getCurrentWeapon() == 2) {
            if (preferences.getBoolean("sound", true)) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ak);
                mediaPlayer.start();
            }
        }
        Shot s = new Shot();
        s.shot = shootFactory.createEntity();
        s.shot.placeAt(shooterBaseRect.centerX(), shooterBaseRect.centerY());
        s.shot.setPosition(shooterGlobalPos);
        s.direction = new Direction(shooterDirection, 1);
        s.shot.setCurrentSprite(0);
        s.shot.setAngleOffSet(0);
        s.shot.setAnimationDivider(100);
        shots.add(s);
    }

    /**
     * Kan lave optimering ved at genbruge shots!!
     */
    public void update() {
        for (Iterator<Shot> it = shots.iterator(); it.hasNext(); ) {
            Shot s = it.next();
            if(s.animationCounter < 5){
                s.animationCounter++;
            }else{
                s.shot.setCurrentSprite(1);
            }

            s.move();
            if (!DataContainer.mapBaseRect.contains(s.shot.getRect())) {
                shootFactory.removeEntity(s.shot);
                it.remove();
//                LL(this, "Deleting shot");
            }
//            LL(this, "updating  shot list");
        }

    }


}
