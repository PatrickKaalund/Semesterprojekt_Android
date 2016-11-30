package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.EnumMap;


class WeaponsHandler {

    private Entity player;
    private AudioPlayer audioPlayer;
    private int ammoAnimationOffset = 25;
    private Entity ammoDrawer;

    private weaponList_e currentWeapon;

    private EnumMap<weaponList_e, Boolean> weaponsAvailable = new EnumMap<>(weaponList_e.class);
    private final EnumMap<weaponList_e, Integer> ammoDmgValues = new EnumMap<>(weaponList_e.class);
    private EnumMap<weaponList_e, Integer> ammoAmounts = new EnumMap<>(weaponList_e.class);

    private final int GUN_CAP = 40;
    private final int SHOTGUN_CAP = 24;
    private final int AK47_CAP = 30;

    /**
     * Player weapons
     */
    enum weaponList_e {
        GUN,
        SHOTGUN,
        AK47,
    }

    WeaponsHandler(Entity player, Context context) {
        this.player = player;

        audioPlayer = new AudioPlayer(context);

        SpriteEntityFactory ammoFactory = new SpriteEntityFactory(R.drawable.ammmo_amount_full_scaled_small, 140, 180, 20, 5, new PointF(950, 260));
        ammoDrawer = ammoFactory.createEntity();

        ammoDrawer.setCurrentSprite(0);

        reset();

    }


    private void reset() {

        weaponsAvailable.put(weaponList_e.GUN, true);
        weaponsAvailable.put(weaponList_e.SHOTGUN, false);
        weaponsAvailable.put(weaponList_e.AK47, false);

        ammoDmgValues.put(weaponList_e.GUN, 3);
        ammoDmgValues.put(weaponList_e.SHOTGUN, 6);
        ammoDmgValues.put(weaponList_e.AK47, 8);

        ammoAmounts.put(weaponList_e.GUN, GUN_CAP);
        ammoAmounts.put(weaponList_e.SHOTGUN, SHOTGUN_CAP);
        ammoAmounts.put(weaponList_e.AK47, AK47_CAP);

        currentWeapon = weaponList_e.GUN;

        update();
    }


    void registerWeaponsDrop(ItemCommon drop) {

        switch (drop.getType()) {

            case AMMO_GUN_DEFAULT :
                if (ammoAmounts.get(weaponList_e.GUN) + drop.size < GUN_CAP)
                    ammoAmounts.put(weaponList_e.GUN, ammoAmounts.get(weaponList_e.GUN) + drop.size);
                else
                    ammoAmounts.put(weaponList_e.GUN, GUN_CAP);
                break;

            case AMMO_SHOTGUN_DEFAULT :
                if (ammoAmounts.get(weaponList_e.SHOTGUN) + drop.size < SHOTGUN_CAP)
                    ammoAmounts.put(weaponList_e.SHOTGUN, ammoAmounts.get(weaponList_e.SHOTGUN) + drop.size);
                else
                    ammoAmounts.put(weaponList_e.SHOTGUN, SHOTGUN_CAP);
                break;

            case AMMO_AK47_DEFAULT:
                if (ammoAmounts.get(weaponList_e.AK47) + drop.size < AK47_CAP)
                    ammoAmounts.put(weaponList_e.AK47, ammoAmounts.get(weaponList_e.AK47) + drop.size);
                else
                    ammoAmounts.put(weaponList_e.AK47, AK47_CAP);
                break;

            case AMMO_BLUE :
                ammoAmounts.put(weaponList_e.GUN, GUN_CAP);
                ammoAmounts.put(weaponList_e.SHOTGUN, SHOTGUN_CAP);
                ammoAmounts.put(weaponList_e.AK47, AK47_CAP);
                break;

            case AMMO_YELLOW :
                ammoAmounts.put(weaponList_e.GUN, 6);
                ammoAmounts.put(weaponList_e.SHOTGUN, 0);
                ammoAmounts.put(weaponList_e.AK47, 0);
                break;
        }

        update();
    }


    void setCurrentWeapon(weaponList_e currentWeapon) {

        audioPlayer.playAudioFromRaw(R.raw.reload);

        switch (currentWeapon) {

            case GUN:
                ammoAnimationOffset = 25;
                player.setAnimationOffset(46);
                break;

            case SHOTGUN:
                ammoAnimationOffset = 0;
                player.setAnimationOffset(23);
                break;

            case AK47:
                ammoAnimationOffset = 66;
                player.setAnimationOffset(0);
                break;
        }

        this.currentWeapon = currentWeapon;

        player.drawNextSprite();

        update();
    }


    public EnumMap<weaponList_e, Integer> getAmmoAmmountValues() {
        return ammoAmounts;
    }


    private EnumMap<weaponList_e, Boolean> getWeaponsAvailable() {
        return weaponsAvailable;
    }


    private void update() {
        ammoDrawer.setCurrentSprite(ammoAmounts.get(currentWeapon) + ammoAnimationOffset);
        player.drawNextSprite();
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


    weaponList_e getCurrentWeapon() {
        return currentWeapon;
    }
}