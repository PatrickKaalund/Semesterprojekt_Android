package com.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
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
import com.services.MusicService;

public class Main extends BaseActivity {

    private SharedPreferences preferences;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this,MusicService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    void doUnbindService() {
        if(musicIsBound) {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        doBindService();

        setContentView(R.layout.activity_main_menu);

        // Check if logged in
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        boolean logged_in = preferences.getBoolean("logged_in", false);

        Log.e("LoginFragment", "Logged in: " + logged_in);

        if (savedInstanceState == null && logged_in) {
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

    @Override
    protected void onPostResume() {
        preferences.edit().putInt("track", R.raw.menu_music).apply();
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }
}
