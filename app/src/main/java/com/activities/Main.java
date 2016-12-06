package com.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.fragments.KeyboardFragment;
import com.fragments.ButtonMainFragment;
import com.fragments.LoginFragment;

public class Main extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        // Check if logged in
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean logged_in = preferences.getBoolean("logged_in", false);

        Log.e("LoginFragment", "Logged in: " + logged_in);

        if (savedInstanceState == null && !logged_in) {
            findViewById(R.id.overlay).setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.start_fragment_holder, new LoginFragment())
                    .add(R.id.start_keyboard_fragment_holder, new KeyboardFragment())
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_menu, new ButtonMainFragment())
                    .commit();
        }
    }
}
