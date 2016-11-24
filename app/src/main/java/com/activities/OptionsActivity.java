package com.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.services.MusicService;

public class OptionsActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
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

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_options);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        soundSwitch = (Switch) findViewById(R.id.switchSound);
        musicSwitch = (Switch) findViewById(R.id.switchMusic);
        onlineSwitch = (Switch) findViewById(R.id.switchOnline);

        TextView back = (TextView) findViewById(R.id.back_button);
        TextView quit = (TextView) findViewById(R.id.quit_button);

        musicSwitch.setChecked(preferences.getBoolean("music", true));
        soundSwitch.setChecked(preferences.getBoolean("sound", true));
        onlineSwitch.setChecked(preferences.getBoolean("online", false));

        soundSwitch.setOnCheckedChangeListener(this);
        musicSwitch.setOnCheckedChangeListener(this);
        onlineSwitch.setOnCheckedChangeListener(this);

        back.setOnClickListener(this);
        quit.setOnClickListener(this);

        doBindService();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        preferences.edit().putInt("window", R.raw.dark_music).apply();
        super.onPostResume();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.quit_button) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if (view.getId() == R.id.back_button) {
            finish();
        }
    }
}
