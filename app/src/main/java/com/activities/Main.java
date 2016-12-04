package com.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.fragments.KeyboardFragment;
import com.fragments.LoginFragment;

public class Main extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);


        if (savedInstanceState == null) {
            findViewById(R.id.overlay).setVisibility(View.VISIBLE);

            Fragment fragment = new LoginFragment();
            Fragment keyboardFragment = new KeyboardFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.start_fragment_holder, fragment)
                    .add(R.id.start_keyboard_fragment_holder, keyboardFragment)
                    .commit();
        }
    }
}
