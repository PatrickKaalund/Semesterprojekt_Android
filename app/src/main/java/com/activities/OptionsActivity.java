package com.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.services.MusicService;

public class OptionsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private Switch soundSwitch, musicSwitch, onlineSwitch;
    private boolean musicIsBound = false;
    private MusicService musicService;
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
        setContentView(R.layout.activity_options);

        soundSwitch = (Switch) findViewById(R.id.switchSound);
        musicSwitch = (Switch) findViewById(R.id.switchMusic);
        onlineSwitch = (Switch) findViewById(R.id.switchOnline);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        musicSwitch.setChecked(preferences.getBoolean("music", false));
        soundSwitch.setChecked(preferences.getBoolean("sound", false));
        onlineSwitch.setChecked(preferences.getBoolean("online", false));

        soundSwitch.setOnCheckedChangeListener(this);
        musicSwitch.setOnCheckedChangeListener(this);
        onlineSwitch.setOnCheckedChangeListener(this);

        doBindService();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (compoundButton == musicSwitch) {
            Log.d("Switch", "music " + musicSwitch.isChecked());
            if (isChecked) {
                musicService.resumeMusic();
                preferences.edit().putBoolean("music", true).apply();
            } else {
                musicService.pauseMusic();
                preferences.edit().putBoolean("music", false).apply();
            }
        }

        else if  (compoundButton == soundSwitch) {
            Log.d("Switch", "sound " + soundSwitch.isChecked());
            if (isChecked)
                preferences.edit().putBoolean("sound", true).apply();
            else
                preferences.edit().putBoolean("sound", false).apply();
        }

        else if  (compoundButton == onlineSwitch) {
            Log.d("Switch", "online " + onlineSwitch.isChecked());
            if (isChecked)
                preferences.edit().putBoolean("online", true).apply();
            else
                preferences.edit().putBoolean("online", false).apply();
        }
    }
}
