package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.EnumMap;


class WeaponsHandler {

    private Player player;
    private AudioPlayer audioPlayer;
    private int ammoAnimationOffset = 25;
    private Entity ammoDrawer;

    private WeaponList_e currentWeapon;

    private EnumMap<WeaponList_e, Boolean> weaponsAvailable = new EnumMap<>(WeaponList_e.class);
    private final EnumMap<WeaponList_e, Integer> ammoDmgValues = new EnumMap<>(WeaponList_e.class);
    private EnumMap<WeaponList_e, Integer> ammoAmounts = new EnumMap<>(WeaponList_e.class);

    private static final int GUN_CAP = 40;
    private static final int SHOTGUN_CAP = 24;
    private static final int AK47_CAP = 30;

    static final int GUN_SPRITE_START = 44;
    private static final int SHOTGUN_SPRITE_START = 22;
    private static final int AK47_SPRITE_START = 0;

    private static final int GUN_SHOT_SPEED = 30;
    private static final int SHOTGUN_SHOT_SPEED = 20;
    private static final int AK47_SHOT_SPEED = 50;

    private static final int GUN_FIRE_RATE = 20;
    private static final int SHOTGUN_FIRE_RATE = 35;
    private static final int AK47_FIRE_RATE = 4;


    /**
     * Player weapons
     */
    enum WeaponList_e {
        GUN(GUN_SPRITE_START, GUN_SHOT_SPEED,GUN_FIRE_RATE),
        SHOTGUN(SHOTGUN_SPRITE_START, SHOTGUN_SHOT_SPEED, SHOTGUN_FIRE_RATE),
        AK47(AK47_SPRITE_START, AK47_SHOT_SPEED, AK47_FIRE_RATE);

        public final int SPRITE_OFFSET;
        public final int FIRE_RATE;
        public final int SHOT_SPEED;


        WeaponList_e(final int SPRITE_OFFSET, final int SHOT_SPEED, final int FIRE_RATE) {
            this.SPRITE_OFFSET = SPRITE_OFFSET;
            this.FIRE_RATE = FIRE_RATE;
            this.SHOT_SPEED = SHOT_SPEED;
        }

    }

    WeaponsHandler(Player player, Context context) {
        this.player = player;

        audioPlayer = new AudioPlayer(context);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        SpriteEntityFactory ammoFactory = new SpriteEntityFactory(R.drawable.ammo, 140, 180, 20, 5, new PointF(displayMetrics.widthPixels - 135, 255));
        ammoDrawer = ammoFactory.createEntity();

        ammoDrawer.setCurrentSprite(0);

        reset();

    }


    public void reset() {

        weaponsAvailable.put(WeaponList_e.GUN, true);
        weaponsAvailable.put(WeaponList_e.SHOTGUN, false);
        weaponsAvailable.put(WeaponList_e.AK47, false);

        ammoDmgValues.put(WeaponList_e.GUN, 3);
        ammoDmgValues.put(WeaponList_e.SHOTGUN, 6);
        ammoDmgValues.put(WeaponList_e.AK47, 8);

        ammoAmounts.put(WeaponList_e.GUN, GUN_CAP);
        ammoAmounts.put(WeaponList_e.SHOTGUN, SHOTGUN_CAP);
        ammoAmounts.put(WeaponList_e.AK47, AK47_CAP);

        currentWeapon = WeaponList_e.GUN;

        update();
    }


    void registerWeaponsDrop(Item drop) {

        switch (drop.getType()) {

            case AMMO_GUN_DEFAULT :
                if (ammoAmounts.get(WeaponList_e.GUN) + drop.size < GUN_CAP)
                    ammoAmounts.put(WeaponList_e.GUN, ammoAmounts.get(WeaponList_e.GUN) + drop.size);
                else
                    ammoAmounts.put(WeaponList_e.GUN, GUN_CAP);
                break;

            case AMMO_SHOTGUN_DEFAULT :
                if (ammoAmounts.get(WeaponList_e.SHOTGUN) + drop.size < SHOTGUN_CAP)
                    ammoAmounts.put(WeaponList_e.SHOTGUN, ammoAmounts.get(WeaponList_e.SHOTGUN) + drop.size);
                else
                    ammoAmounts.put(WeaponList_e.SHOTGUN, SHOTGUN_CAP);
                break;

            case AMMO_AK47_DEFAULT:
                if (ammoAmounts.get(WeaponList_e.AK47) + drop.size < AK47_CAP)
                    ammoAmounts.put(WeaponList_e.AK47, ammoAmounts.get(WeaponList_e.AK47) + drop.size);
                else
                    ammoAmounts.put(WeaponList_e.AK47, AK47_CAP);
                break;

            case AMMO_BLUE :
                ammoAmounts.put(WeaponList_e.GUN, GUN_CAP);
                ammoAmounts.put(WeaponList_e.SHOTGUN, SHOTGUN_CAP);
                ammoAmounts.put(WeaponList_e.AK47, AK47_CAP);
                break;

            case AMMO_YELLOW :
                ammoAmounts.put(WeaponList_e.GUN, GUN_CAP);
                ammoAmounts.put(WeaponList_e.SHOTGUN, SHOTGUN_CAP);
                ammoAmounts.put(WeaponList_e.AK47, AK47_CAP);
                break;
        }

        update();
    }


    void setCurrentWeapon(WeaponList_e currentWeapon) {

        audioPlayer.playAudioFromRaw(R.raw.reload);

        switch (currentWeapon) {

            case GUN:
                ammoAnimationOffset = 25;
                break;

            case SHOTGUN:
                ammoAnimationOffset = 0;
                break;

            case AK47:
                ammoAnimationOffset = 66;
                break;
        }

        this.currentWeapon = currentWeapon;

        player.setWeapon(currentWeapon);

        update();
    }


    public EnumMap<WeaponList_e, Integer> getAmmoAmmountValues() {
        return ammoAmounts;
    }


    private EnumMap<WeaponList_e, Boolean> getWeaponsAvailable() {
        return weaponsAvailable;
    }


    private void update() {
        ammoDrawer.setCurrentSprite(ammoAmounts.get(currentWeapon) + ammoAnimationOffset);
    }


    int getDmgValue() {
        return ammoDmgValues.get(currentWeapon);
    }


    void setCurrentAmmoAmount(int amount) {
        ammoAmounts.put(currentWeapon, amount);
        update();
    }

    int getCurrentAmmoAmount() {
        return ammoAmounts.get(currentWeapon);
    }


    WeaponList_e getCurrentWeapon() {
        return currentWeapon;
    }
}