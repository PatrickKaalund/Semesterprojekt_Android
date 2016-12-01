package com.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.fragments.LoginFragment;
import com.graphics.FPSMeasuring;

public class Main extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu_frag);


        if (savedInstanceState == null) {
            findViewById(R.id.overlay).setVisibility(View.VISIBLE);

            Fragment fragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_login, fragment)  // tom container i fragment_multiplayer
                    .commit();
        }
    }
}
