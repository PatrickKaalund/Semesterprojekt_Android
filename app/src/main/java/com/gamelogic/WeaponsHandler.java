package com.gamelogic;

import android.content.Context;
import android.graphics.PointF;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.graphics.Entity;
import com.graphics.SpriteEntityFactory;

import java.util.EnumMap;


public class WeaponsHandler {

    private Entity player;
    private AudioPlayer audioPlayer;

    private Entity ammoDrawer;

    private weaponList_e currentWeapon;

    private EnumMap<weaponList_e, Boolean> weaponsAvailable = new EnumMap<>(weaponList_e.class);
    private final EnumMap<weaponList_e, Integer> ammoDmgValues = new EnumMap<>(weaponList_e.class);
    private EnumMap<weaponList_e, Integer> ammoAmounts = new EnumMap<>(weaponList_e.class);

    private final int GUN_CAP = 40;
    private final int SHOTGUN_CAP = 25;
    private final int AK47_CAP = 30;

    /**
     * Player weapons
     */
    public enum weaponList_e {
        GUN,
        SHOTGUN,
        AK47,
    }

    public WeaponsHandler(Entity player, Context context) {
        this.player = player;

        audioPlayer = new AudioPlayer(context);

        SpriteEntityFactory ammoFactory = new SpriteEntityFactory(R.drawable.ammmo_amount, 110, 150, 41, 1, new PointF(950, 260));

        ammoDrawer = ammoFactory.createEntity();

        ammoDrawer.setCurrentSprite(0);

        reset();
    }


    public void reset() {
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


    public void registerWeaponsDrop(ItemCommon drop) {

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

        ammoDrawer.setCurrentSprite(getCurrentAmmoAmount());
    }


    public void setCurrentWeapon(weaponList_e currentWeapon) {

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

        update();
    }


    public EnumMap<weaponList_e, Integer> getAmmoAmmountValues() {
        return ammoAmounts;
    }


    private EnumMap<weaponList_e, Boolean> getWeaponsAvailable() {
        return weaponsAvailable;
    }


    private void update() {
        ammoDrawer.setCurrentSprite(ammoAmounts.get(currentWeapon));
    }


    public int getDmgValue() {
        return ammoDmgValues.get(currentWeapon);
    }


    public void setCurrentAmmoAmount(int amount) {
        ammoAmounts.put(currentWeapon, amount);
        update();
    }

    public int getCurrentAmmoAmount() {
        return ammoAmounts.get(currentWeapon);
    }


    public weaponList_e getCurrentWeapon() {
        return currentWeapon;
    }
}