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
    Context context;
    public Control(Context context, Game game){
        this.context = context;
        this.game = game;
        Log.d("Joystick", "started");

        joystickValues = new ArrayList<>();

        // fix this? :-)
        joystickValues.add(0);
        joystickValues.add(0);
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

    public void setShootButton(FloatingActionButton shootButton) {
        shootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Shoot_button", "Bang!");
            }
        });
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
