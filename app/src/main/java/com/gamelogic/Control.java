package com.gamelogic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.activities.OptionsActivity;
import com.core.Game;
import com.views.DropDownMenu;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

import static com.views.DropDownMenu.STATE_PRESSED;
import static java.security.AccessController.getContext;

public class Control {

    private ArrayList<Integer> joystickValues;
    private Game game;
    private Context context;



    private boolean shooting = false;

    public Control(Context context, Game game){
        this.context = context;
        this.game = game;
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
                               Intent settings = new Intent(context, OptionsActivity.class);
                               ((Activity) context).startActivity(settings);
                           } else if (menuItem == 2) {
                               Log.d("DropDownPressed", "Rifle");
                               dropDownMenu.closeMenu();
                           } else if (menuItem == 3) {
                               Log.d("DropDownPressed", "Shotgun");
                               dropDownMenu.closeMenu();
                           } else if (menuItem == 4) {
                               Log.d("DropDownPressed", "Sidearm");
                               dropDownMenu.closeMenu();
                           }
                       } return true;
                   }
               } return false;
           }
       });
    }

    public void gameStart() {
        game.gameStart();
    }

    public ArrayList<Integer> getJoystickValues(){
        return this.joystickValues;
    }
}
