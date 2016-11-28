package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

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


/**
 * Created by PatrickKaalund on 13/10/2016.
 */

public class Player extends PlayerCommon {

    private Entity player;
    private Context context;
    private NetworkHandler networkHandler;
    private ArrayList<Integer> joystickValues;
    private Shooter gun;
    private int shotSpeedCounter = 0;
    private int shotSpeed = 10;
    private DirectionLock directionLock;
    private Direction mapDirection;
    public int playerLock;
    public int playerTLBR;
    private Entity healthDrawer;
    private int playerID;

    private WeaponsHandler weaponsHandler;

    private Direction direction;


    /**
     * A playable character on the screen
     * @param context
     * @param networkHandler
     */
    public Player(Context context, NetworkHandler networkHandler,PointF startPos) {
        // ----- Misc -----

        this.context = context;
        this.networkHandler = networkHandler;

        joystickValues = new ArrayList<>();
        DataContainer.player = this;

        // ----- Player stuff -----
        player = super.player;
//        player.placeAt(context.getResources().getDisplayMetrics().widthPixels / 2, context.getResources().getDisplayMetrics().heightPixels / 2);
//        player.placeAt(startPos.x,startPos.y);
//        player.setPosition(new PointF(startPos.x /*+ context.getResources().getDisplayMetrics().widthPixels/2*/, startPos.y /*+ context.getResources().getDisplayMetrics().heightPixels/2*/));
        player.placeAt(startPos.x, startPos.y);
        player.setPosition(new PointF(2000.0F, 2000.0F));
        direction = new Direction(super.speed);

        weaponsHandler = new WeaponsHandler(player, context);

        // ----- Gun -----
        gun = new Shooter(weaponsHandler);
        shotSpeedCounter = shotSpeed;

        // ---- Map ----
        directionLock = new DirectionLock();
        mapDirection = new Direction(direction);
        mapDirection.tag = 2;

        SpriteEntityFactory healthFactory = new SpriteEntityFactory(R.drawable.numbers_red, 160, 160, 1, 11, new PointF(300, 125));
        healthDrawer = healthFactory.createEntity();
        healthDrawer.setCurrentSprite(super.health / 10);
    }


    /**
     * Update player state. Checks if player is shooting and if enemy is hit by a shot
     * @param control
     * @param enemys
     */
    public void update(Control control, EnemySpawner enemys) {
//        LL(this,"update player");
        if (shotSpeedCounter > shotSpeed && control.isShooting()) {
            gun.shoot(player.getPosition(), player.getRect(), direction, weaponsHandler.getCurrentWeapon());
            shotSpeedCounter = 0;
        } else {
            shotSpeedCounter++;
        }
        gun.update(enemys, weaponsHandler.getDmgValue());
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
                    map.move(mapDirection,this);//Move player on background
                    player.move(direction);//Move the player and update player global and local position
                    break;
                case X_LOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.getPosition().y += direction.velocity_Y;
                    player.moveBy(direction);//Move the player and update player global and local position

                    break;
                case Y_LOCKED:
                    map.move(mapDirection,this);//Move player on background
                    player.getPosition().x += direction.velocity_X;
                    player.moveBy(direction);//Move the player and update player global and local position

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

            if(DataContainer.multiplayerGame){
                networkHandler.updatePlayerPosition(player.getPosition().x, player.getPosition().y, joystick_angle); // Update player pos on firebase
            }
        } else {
            DataContainer.mapMovement.x = 0;
            DataContainer.mapMovement.y = 0;

            switch (weaponsHandler.getCurrentWeapon()) { //Set weapon sprite

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
     *  The player ID
     * @return int
     */
    public int getPlayerID() {
        return this.playerID;
    }

    public void setPlayerID(int playerID) { this.playerID = playerID; }

    /**
     * Damege the player. Changes state for the player
     * @param damage
     */
    @Override
    public void doDamage(int damage) {
        super.health -= damage;

        healthDrawer.setCurrentSprite(super.health / 10);

        Log.d("TAKING DAMAGE", "Damage: " + damage + " Health: " + super.health);

//        if (super.health <= 0) {
//            Log.e("PLAYER IS DEAD", "+++++++++++++++++++++++++  PLAYER IS DEAD ++++++++++++++++++++");
//        }
    }

    /**
     *
     * @return
     */
    public WeaponsHandler getWeaponsHandler() {
        return weaponsHandler;
    }
}
