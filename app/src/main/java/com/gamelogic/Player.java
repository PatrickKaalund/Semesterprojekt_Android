package com.gamelogic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.BackgroundEntity;
import com.graphics.Direction;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;
import com.network.Firebase.NetworkHandler;

import java.util.ArrayList;

import static com.gamelogic.DirectionLock.ALL_LOCKED;
import static com.gamelogic.DirectionLock.UNLOCKED;
import static com.gamelogic.DirectionLock.X_LOCKED;
import static com.gamelogic.DirectionLock.Y_LOCKED;
import static com.graphics.GraphicsTools.LL;

/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {
    /**
     * Player weapons
     */
    public enum weaponSelection_e {
        GUN,
        SHOTGUN,
        AK47,
        ALL_GUNS,
    }


    private Entity player;
    private Context context;
    private NetworkHandler networkHandler;
    private ArrayList<Integer> joystickValues;
    private Shooter gun;
    private int shotSpeedCounter = 0;
    private int shotSpeed = 10;
    private weaponSelection_e currentWeapon = weaponSelection_e.GUN;
    private DirectionLock directionLock;
    private Direction mapDirection;
    private SharedPreferences preferences;
    public int playerLock;
    public int playerTLBR;
    private AudioPlayer audioPlayer;


    /**
     * A playable character on the screen
     * @param context
     * @param networkHandler
     */
    public Player(Context context, NetworkHandler networkHandler,PointF startPos) {
        // ----- Misc -----

        this.context = context;
        this.networkHandler = networkHandler;

        audioPlayer = new AudioPlayer(context);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        joystickValues = new ArrayList<>();
        DataContainer.player = this;

        // ----- Player stuff -----
        player = super.player;
//        player.placeAt(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2);
//        player.placeAt(startPos.x,startPos.y);
//        player.setPosition(new PointF(startPos.x /*+ context.getResources().getDisplayMetrics().widthPixels/2*/, startPos.y /*+ context.getResources().getDisplayMetrics().heightPixels/2*/));
        player.placeAt(startPos.x, startPos.y);
        player.setPosition(new PointF(2000.0F, 2000.0F));
        // ----- Gun -----
        gun = new Shooter();
        shotSpeedCounter = shotSpeed;

        // ---- Map ----
        directionLock = new DirectionLock();
        mapDirection = new Direction(super.direction);
        mapDirection.tag = 2;

    }


    /**
     * Update player state. Checks if player is shooting and if enemy is hit by a shot
     * @param control
     * @param enemys
     */
    public void update(Control control, EnemySpawner enemys) {
//        LL(this,"update player");
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
        int joystick_angle = joystickValues.get(0);

        if (joystick_strength > 0) { //Do only if valid input

            super.direction.set(joystick_angle, joystick_strength);//Set velocity and angle for player
            mapDirection.set(joystick_angle, joystick_strength);//Set velocity and angle for map background

            playerLock = directionLock.check( // Check if player is colliding with screen boarder
                    super.direction,
                    map.getInnerBoarder(),
                    player.getRect().centerX(),
                    player.getRect().centerY()
            );
            playerTLBR = directionLock.getTblr();



            switch (playerLock) {
                case UNLOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.move(super.direction);//Move the player and update player global and local position
                    break;
                case X_LOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.getPosition().y += super.direction.velocity_Y;
                    player.moveBy(super.direction);//Move the player and update player global and local position

                    break;
                case Y_LOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.getPosition().x += super.direction.velocity_X;
                    player.moveBy(super.direction);//Move the player and update player global and local position

                    break;
                case ALL_LOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.moveBy(0,0,direction.getAngle());
                    break;
                default:
                    Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
                    break;
            }


            player.drawNextSprite();//Animate




        } else {
            DataContainer.mapMovement.x = 0;
            DataContainer.mapMovement.y = 0;

            switch (currentWeapon) { //Set weapon sprite

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

    /**
     *  Players base resct. Rect that is drawn on the screen
     * @return RectF
     */
    public RectF getRect() {
        return this.player.getRect();
    }

    /**
     * Players current pos
     * @return PointF
     */
    public PointF getPos() {
        return this.player.getPosition();
    }

    /**
     * The player Entity
     * @return Entity
     */
    public Entity getPlayerEntity() {
        return player;
    }

    /**
     * Damege the player. Changes state for the player
     * @param damage
     */
    @Override
    public void doDamage(int damage) {
        super.health -= damage;
//        if (super.health <= 0) {
//            Log.e("PLAYER IS DEAD", "+++++++++++++++++++++++++  PLAYER IS DEAD ++++++++++++++++++++");
//        }

    }

    /**
     *
     * @return
     */
    public weaponSelection_e getCurrentWeapon() {
        return currentWeapon;
    }

    /**
     *
     * @param currentWeapon
     */
    public void setCurrentWeapon(weaponSelection_e currentWeapon) {

        audioPlayer.playAudioFromRaw(R.raw.reload);

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
