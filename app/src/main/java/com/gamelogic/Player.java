package com.gamelogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.NetworkHandler;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {

    public enum weaponSelection_e {
        GUN,
        SHOTGUN,
        AK47,
        ALL_GUNS,
    }


    private Entity player;
    private NetworkHandler networkHandler;
    private ArrayList<Integer> joystickValues;
    private Shooter gun;
    private int shotSpeedCounter = 0;
    private int shotSpeed = 10;
    private weaponSelection_e currentWeapon = weaponSelection_e.GUN;
    private DirectionLock directionLock;
    private Direction mapDirection;
    private SharedPreferences preferences;
    private final Context context;
    int playerLock;


    public Player(Context context, NetworkHandler networkHandler) {
        this.context = context;
        this.networkHandler = networkHandler;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        player = super.player;
        player.placeAt(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2);
        player.setPosition(new PointF(2000, 2000));
        super.direction.lock = 0;

        joystickValues = new ArrayList<>();

//        displayMetrics = context.getResources().getDisplayMetrics();

        DataContainer.player = this;

        gun = new Shooter();
        shotSpeedCounter = shotSpeed;
        directionLock = new DirectionLock();
        super.direction.tag = 1;
        mapDirection = new Direction(super.direction);
        mapDirection.tag = 2;

    }

    public void update(Control control, EnemySpawner enemys) {
        if (shotSpeedCounter > shotSpeed && control.isShooting()) {
            gun.shoot(player.getPosition(), player.getRect(), super.direction, currentWeapon);
            shotSpeedCounter = 0;
        } else {
            shotSpeedCounter++;
        }
        gun.update(enemys);
    }

    /**
     * Move player with control input on a background
     * @param control
     * @param map
     */
    public void move(Control control, BackgroundEntity map) {

        // read joystick
        joystickValues = control.getJoystickValues();
        int joystick_strength = (joystickValues.get(1));

        if (joystick_strength > 0) {

            int joystick_angle = joystickValues.get(0);
            super.direction.set(joystick_angle, joystick_strength);
            mapDirection.set(joystick_angle, joystick_strength);

            playerLock = directionLock.check(
                    super.direction,
                    map.getInnerBoarder(),
                    player.getRect().centerX(),
                    player.getRect().centerY()
            );
            player.move(super.direction);
            map.move(mapDirection, playerLock, directionLock.getTblr());
            player.drawNextSprite();

        } else {

            switch (currentWeapon) {

                case GUN:
                    player.setCurrentSprite(45);

                    break;
                case SHOTGUN:
                    player.setCurrentSprite(23);

                    break;
                case AK47:
                    player.setCurrentSprite(0);

                    break;
                default:
                    Log.e("PLAYER", "DEFULTED IN PLAYE::move: switch (currentWeapon)");
                    break;
            }
        }

//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    private void move(float deltaX, float deltaY, int angle) {
        player.getPosition().x += deltaX;
        player.getPosition().y += deltaY;
        player.moveBy(deltaX, deltaY, angle);
    }

    private void moveX(float deltaX, int angle) {
        player.moveBy(deltaX, 0, angle);
//        player.getPosition().x += deltaX;
    }

    private void moveY(float deltaY, int angle) {
        player.moveBy(0, deltaY, angle);
//        player.getPosition().y += deltaY;
    }

    public RectF getRect() {
        return this.player.getRect();
    }

    public PointF getPos() {
        return this.player.getPosition();
    }

    public Entity getPlayer() {
        return player;
    }

    @Override
    public void doDamge(int damge) {
        super.health -= damge;
        if (super.health <= 0) {
            Log.e("PLAYER IS DEAD", "+++++++++++++++++++++++++  PLAYER IS DEAD ++++++++++++++++++++");
        }

    }

    public Entity getEntity() {
        return player;
    }

    public weaponSelection_e getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(weaponSelection_e currentWeapon) {

        if (preferences.getBoolean("sound", true)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.reload);
            mediaPlayer.start();
        }

        int currentSprite = player.getCurrentSprite();
        switch (currentWeapon) {

            case GUN:
                player.setAnimationOrder(new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64});
                
                switch (this.currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite + 23);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite + 46);
                        break;
                }
                break;

            case SHOTGUN:
                player.setAnimationOrder(new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42});

                switch (this.currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite - 23);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite + 23);
                        break;
                }
                break;

            case AK47:
                player.setAnimationOrder(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});

                switch (this.currentWeapon) {
                    case GUN:
                        player.setCurrentSprite(currentSprite - 46);
                        break;
                    case SHOTGUN:
                        player.setCurrentSprite(currentSprite - 23);
                        break;
                    case AK47:
                        player.setCurrentSprite(currentSprite);
                        break;
                }
                break;
        }
        this.currentWeapon = currentWeapon;
    }
}
