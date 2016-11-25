package com.gamelogic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.activities.OptionsActivity;
import com.core.Game;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.views.DropDownMenu;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static com.views.DropDownMenu.STATE_PRESSED;

public class Control {

    private ArrayList<Integer> joystickValues;
    private Game game;
    private Context context;
    private SharedPreferences preferences;

    private boolean shooting = false;

    public Control(Context context, Game game) {
        this.context = context;
        this.game = game;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("Joystick", "started");

        joystickValues = new ArrayList<>();

        joystickValues.add(0, 0);
        joystickValues.add(0, 1);
    }

    public void setJoystick(JoystickView joystickView) {
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                joystickValues.set(0, angle);
                joystickValues.set(1, strength);
//                Log.d("Joystick_angle", Integer.toString(angle));
//                Log.d("Joystick_strength", Integer.toString(strength));
            }
        });
    }

    private float lastX = 0;
    private float lastY = 0;

    public void setShootButton(FloatingActionButton shootButton) {
        shootButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d("ShootPressed", "Shooting");
                    shooting = true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("ShootPressed", "Stopped shooting");
                    shooting = false;
                } else {
                    if (lastX < event.getAxisValue(MotionEvent.AXIS_X))
                        Log.d("MoveEventX", "Right");
                    else if (lastX > event.getAxisValue(MotionEvent.AXIS_X))
                        Log.d("MoveEventX", "Left");
                    else if (lastY < event.getAxisValue(MotionEvent.AXIS_Y))
                        Log.d("MoveEventX", "Down");
                    else if (lastX > event.getAxisValue(MotionEvent.AXIS_Y))
                        Log.d("MoveEventX", "Up");

                    lastX = event.getAxisValue(MotionEvent.AXIS_X);
                    lastY = event.getAxisValue(MotionEvent.AXIS_Y);
                }
                return true;
            }
        });
    }

    public boolean isShooting() {
        return shooting;
    }

    public void setInventoryButton(final DropDownMenu dropDownMenu) {
        dropDownMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    int menuItem = dropDownMenu.isMenuItemTouched(event);
                    if (dropDownMenu.isMenuVisible && menuItem > 0) {

                        if (menuItem <= dropDownMenu.mDrawableArray.size()) {
                            dropDownMenu.mDrawableArray.get(dropDownMenu.mMenuPoints.size() - menuItem).setState(STATE_PRESSED);
                            dropDownMenu.invalidate();

                            if (menuItem == 1) {
                                Log.d("DropDownPressed", "Options");
                                dropDownMenu.closeMenu();
                                if (preferences.getBoolean("sound", true)) {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.click);
                                    mediaPlayer.start();
                                }
                                Intent settings = new Intent(context, OptionsActivity.class);
                                context.startActivity(settings);
                            }
                            else if (menuItem == 2) {
                                Log.d("DropDownPressed", "Rifle");
                                dropDownMenu.closeMenu();

                                if (preferences.getBoolean("sound", true)) {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.reload);
                                    mediaPlayer.start();
                                }

                                game.getPlayer().getEntity().setAnimationOrder(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});
                                game.getPlayer().setCurrentWeapon(2);

                                int currentSprite = game.getPlayer().getEntity().getCurrentSprite();
                                if (game.getPlayer().getCurrentWeapon() == 0) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite - 46);
                                } else if (game.getPlayer().getCurrentWeapon() == 1) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite - 23);
                                } else if (game.getPlayer().getCurrentWeapon() == 2) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite);
                                }

                                game.map.move(0, 0);
                            } else if (menuItem == 3) {
                                Log.d("DropDownPressed", "Shotgun");
                                dropDownMenu.closeMenu();

                                if (preferences.getBoolean("sound", true)) {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.reload);
                                    mediaPlayer.start();
                                }

                                game.getPlayer().getEntity().setAnimationOrder(new int[]{23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42});
                                game.getPlayer().setCurrentWeapon(1);

                                int currentSprite = game.getPlayer().getEntity().getCurrentSprite();
                                if (game.getPlayer().getCurrentWeapon() == 0) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite - 23);
                                } else if (game.getPlayer().getCurrentWeapon() == 1) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite);
                                } else if (game.getPlayer().getCurrentWeapon() == 2) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite + 23);
                                }

                                game.map.move(0, 0);
                            } else if (menuItem == 4) {
                                Log.d("DropDownPressed", "Sidearm");
                                dropDownMenu.closeMenu();

                                if (preferences.getBoolean("sound", true)) {
                                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.reload);
                                    mediaPlayer.start();
                                }

                                game.getPlayer().getEntity().setAnimationOrder(new int[]{45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64});
                                game.getPlayer().setCurrentWeapon(0);

                                int currentSprite = game.getPlayer().getEntity().getCurrentSprite();
                                if (game.getPlayer().getCurrentWeapon() == 0) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite);
                                } else if (game.getPlayer().getCurrentWeapon() == 1) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite + 23);
                                } else if (game.getPlayer().getCurrentWeapon() == 2) {
                                    game.getPlayer().getEntity().setCurrentSprite(currentSprite + 46);
                                }

                                game.map.move(0, 0);
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void gameStart() {
        game.gameStart();
    }

    public ArrayList<Integer> getJoystickValues() {
        return this.joystickValues;
    }
}
