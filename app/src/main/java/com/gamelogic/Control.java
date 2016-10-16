package com.gamelogic;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Control {

    public ArrayList<Integer> joystickValues;

    public Control(Context context){
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

    public ArrayList<Integer> getJoystickValues(){
        return this.joystickValues;
    }
}
