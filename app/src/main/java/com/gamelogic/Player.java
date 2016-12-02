package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
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
import static com.gamelogic.WeaponsHandler.*;


/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {

    private Entity player;
    private Context context;
    private NetworkHandler networkHandler;
    private ArrayList<Integer> joystickValues;
    private Shooter firearm;
    private int fireRateCounter = 0;
    private int shotSpeed = 10;
    private DirectionLock directionLock;
    private Direction mapDirection;
    public int playerLock;
    public int playerTLBR;
    private Entity healthDrawer;
    private Entity livesDrawer;
    private int playerID;
    private AudioPlayer audioPlayer;
    private WeaponsHandler weaponsHandler;
    public PlayerStates_e currentState;

    private Direction direction;
    private int stateCounter = 0;
    public WeaponList_e currentWeapon;
    public static final int EMTY_SPRITE = 21;
    public static final int BLINK_RATE = 8;
    private boolean moved = false;
    private int lastMoveSprite;


    public enum PlayerStates_e {
        NORMAL(0, 0),
        HIT(66, 10),
        IMMORTAL(0, 80),
        GAME_OVER(0, 20);


        private final int spriteOffset;
        private final int stateCount;

        PlayerStates_e(int val, int animationCounter) {
            spriteOffset = val;
            stateCount = animationCounter;
        }
    }


    /**
     * A playable character on the screen
     *
     * @param context
     * @param networkHandler
     */
    public Player(Context context, NetworkHandler networkHandler, PointF startPos) {
        // ----- Misc -----
        this.context = context;
        this.networkHandler = networkHandler;
        audioPlayer = new AudioPlayer(context);
        joystickValues = new ArrayList<>();
        DataContainer.instance.player = this;

        // ----- Player stuff -----
        player = super.player;
        player.setAngleOffSet(0);
        player.setAnimationDivider(1);
        player.setAnimationOrder(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});

//        player.placeAt(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2);
//        player.placeAt(startPos.x,startPos.y);
//        player.setPosition(new PointF(startPos.x /*+ context.getResources().getDisplayMetrics().widthPixels/2*/, startPos.y /*+ context.getResources().getDisplayMetrics().heightPixels/2*/));
        player.placeAt(startPos.x, startPos.y);
        player.setPosition(new PointF(2000.0F, 2000.0F));
        direction = new Direction(super.speed);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        SpriteEntityFactory healthFactory = new SpriteEntityFactory(R.drawable.health, 80, 220, 21, 1, new PointF(125, 260));
        SpriteEntityFactory livesFactory = new SpriteEntityFactory(R.drawable.lives, 25, 170, 4, 1, new PointF(130, 205));

        healthDrawer = healthFactory.createEntity();
        healthDrawer.setCurrentSprite(super.health / 5);

        livesDrawer = livesFactory.createEntity();
        livesDrawer.setCurrentSprite(super.lives);


        // ----- Gun -----
        weaponsHandler = new WeaponsHandler(this, context);
        firearm = new Shooter(weaponsHandler);
        fireRateCounter = shotSpeed;

        // ---- Map ----
        directionLock = new DirectionLock();
        mapDirection = new Direction(direction);
        mapDirection.tag = 2;


        currentState = PlayerStates_e.NORMAL;

        weaponsHandler.setCurrentWeapon(WeaponList_e.GUN);
    }


    /**
     * Update player state. Checks if player is shooting and if enemy is hit by a shot
     *
     * @param control
     * @param enemys
     */
    public void update(Control control, EnemySpawner enemys) {
//        LL(this,"update player");
        if (fireRateCounter > currentWeapon.FIRE_RATE && control.isShooting()) {
            firearm.shoot(player.getPosition(), player.getRect(), direction, weaponsHandler.getCurrentWeapon());
            fireRateCounter = 0;
        } else {
            fireRateCounter++;
        }
        firearm.update(enemys, weaponsHandler.getDmgValue());

        switch (currentState) {

            case NORMAL:

                break;
            case HIT:
                if (++stateCounter >= currentState.stateCount) {
                    currentState = PlayerStates_e.NORMAL;
                    player.setAnimationOffset(getSpriteOffset());
                }
                break;
            case IMMORTAL:

                lastMoveSprite = player.getNextSprite();

                if (++stateCounter >= currentState.stateCount) {
                    currentState = PlayerStates_e.NORMAL;
                    player.setAnimationOffset(getSpriteOffset());
                } else {
                    if (stateCounter % BLINK_RATE < BLINK_RATE/2) {
                        player.setCurrentSprite(EMTY_SPRITE);
                    } else {
                        player.setCurrentSprite(lastMoveSprite);
                    }
                }
                break;
        }
    }


    /**
     * Move player with control input on a background
     *
     * @param control
     * @param map
     */
    public void move(Control control, BackgroundEntity map) {

        // read joystick
        joystickValues = control.getJoystickValues();
        int joystick_strength = (joystickValues.get(1));
        int joystick_angle = joystickValues.get(0);

        if (joystick_strength > 0) { //Do only if valid input

            direction.set(joystick_angle, joystick_strength);//Set velocity and angle for player
            mapDirection.set(joystick_angle, joystick_strength);//Set velocity and angle for map background

            playerLock = directionLock.check( // Check if player is colliding with screen boarder
                    direction,
                    map.getInnerBoarder(),
                    player.getRect().centerX(),
                    player.getRect().centerY()
            );
            playerTLBR = directionLock.getTblr();


            switch (playerLock) {
                case UNLOCKED:
                    map.move(mapDirection, this);//Move player on background
                    player.move(direction);//Move the player and update player global and local position
                    break;
                case X_LOCKED:
                    map.move(mapDirection, this);//Move player on background
                    player.getPosition().y += direction.velocity_Y;
                    player.moveBy(direction);//Move the player and update player global and local position

                    break;
                case Y_LOCKED:
                    map.move(mapDirection, this);//Move player on background
                    player.getPosition().x += direction.velocity_X;
                    player.moveBy(direction);//Move the player and update player global and local position

                    break;
                case ALL_LOCKED:
                    map.move(mapDirection, this);//Move player on background
                    player.moveBy(0, 0, direction.getAngle());
                    break;
                default:
                    Log.e(this.getClass().getCanonicalName(), "Defaulted in Player::move()");
                    break;
            }
            player.drawNextSprite();
            moved = true;


            if (DataContainer.instance.multiplayerGame) {
                networkHandler.updatePlayerPosition(player.getPosition().x, player.getPosition().y, joystick_angle); // Update player pos on firebase
            }
        } else {
            DataContainer.instance.mapMovement.x = 0;
            DataContainer.instance.mapMovement.y = 0;
        }


//        Log.d("Player", "Angle: " + joystick_angle);
//        Log.d("Player", "Strength: " + joystick_strength);
    }

    /**
     * Players base resct. Rect that is drawn on the screen
     *
     * @return RectF
     */
    public RectF getRect() {
        return this.player.getRect();
    }

    /**
     * Players current pos
     *
     * @return PointF
     */
    public PointF getPos() {
        return this.player.getPosition();
    }

    /**
     * The player Entity
     *
     * @return Entity
     */
    public Entity getPlayerEntity() {
        return player;
    }

    /**
     * The player ID
     *
     * @return int
     */
    public int getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Damege the player. Changes state for the player
     *
     * @param damage
     */
    @Override
    public boolean doDamage(int damage) {
        if (currentState == PlayerStates_e.NORMAL) {
            super.health -= damage;

            if (super.health <= 0) {
                lives--;
                if (lives <= 0) {
                    currentState = PlayerStates_e.GAME_OVER;
                } else {
                    currentState = PlayerStates_e.IMMORTAL;
                    super.health = BASE_HEALTH;
                    healthDrawer.setCurrentSprite(super.health / 5);
                    weaponsHandler.setCurrentWeapon(WeaponList_e.GUN);
                    player.setCurrentSprite(WeaponsHandler.GUN_SPRITE_START);
                    stateCounter = 0;
                }
                livesDrawer.setCurrentSprite(lives % LIVES_TOTAL);
            } else {
                healthDrawer.setCurrentSprite(super.health / 5);
                //            Log.d("TAKING DAMAGE", "Damage: " + damage + " Health: " + super.health);
                currentState = PlayerStates_e.HIT;
                player.setAnimationOffset(getSpriteOffset());
                stateCounter = 0;
                //  player.setCurrentSprite(player.getCurrentSprite()+getSpriteOffset());
//            Log.d("PLAYER doDamage", "currentState " + currentState.toString());
                return true;
            }
        }
        return false;
    }

    /**
     * Return WeaponsHandler
     *
     * @return
     */
    public WeaponsHandler getWeaponsHandler() {
        return weaponsHandler;
    }

    public void registerPickup(ItemCommon item) {

        if (item.getType().ordinal() > 0) {
            audioPlayer.playAudioFromRaw(R.raw.reload);

            weaponsHandler.registerWeaponsDrop(item);
        } else if (item.getType() == ItemCommon.ItemList_e.MEDIC) {
            audioPlayer.playAudioFromRaw(R.raw.medic);

            super.health += item.size;
            if (super.health > 100)
                super.health = 100;

            healthDrawer.setCurrentSprite(super.health / 5);

            Log.d("PLAYER", "RECIEVED HEALTH: " + item.size + " HEALTH IS: " + super.health);
        }
    }

    private int getSpriteOffset() {
        return currentState.spriteOffset + currentWeapon.SPRITE_OFFSET;
    }


    public void setWeapon(WeaponList_e weapon) {
        currentWeapon = weapon;
        player.setAnimationOffset(getSpriteOffset());
    }

}
