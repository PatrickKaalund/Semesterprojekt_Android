package com.gamelogic;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class Control {

    public Control(Context context){
        Log.d("Joystick", "started");
    }

    public void setJoystick(JoystickView joystickView) {
        joystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                Log.d("Joystick_angle", Integer.toString(angle));
                Log.d("Joystick_strength", Integer.toString(strength));
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
}
