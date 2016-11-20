package com.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.services.MusicService;

import java.util.List;

public class BaseActivity extends AppCompatActivity {

    boolean musicIsBound = false;
    static MusicService musicService;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBindService();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        doUnbindService();
        super.onPause();
    }

    @Override
    protected void onResume() {
        doBindService();
        super.onPostResume();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                serviceConnection, BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    void doUnbindService() {
        if (musicIsBound) {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }




}