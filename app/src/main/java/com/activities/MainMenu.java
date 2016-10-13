package com.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.teststuff.MapTestActivity;

import com.core.GameLoop;
import com.services.MusicService;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    boolean musicIsBound = false;
    MusicService musicService;
    Button play, settings, mapTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Add music boolean to preference manager if not present
        if (!preferences.contains("music")) {
            preferences.edit()
                    .putBoolean("music", false) // Music disabled by default
                    // ToDO Add default preferences
                    .apply();
        }
        //preferences.edit().putBoolean("music", true).apply();     // Enable music (debug)
        doBindService();


        play = (Button) findViewById(R.id.buttonPlay);
        settings = (Button) findViewById(R.id.buttonSettings);
        mapTest = (Button) findViewById(R.id.buttonMapTest);

        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        mapTest.setOnClickListener(this);
    }

    // Kunne undlades..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_bar_options_main, menu);
        return true;
    }

    // Kunne undlades..
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options) {
            // Todo Implement options
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.quit)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    // Handle music when Home is pressed
    @Override
    protected void onPause() {
        if(musicIsBound) {
            musicService.pauseMusic();
            musicIsBound = false;
        }
        super.onPause();
    }

    // Handle music when app is resumed
    @Override
    protected void onPostResume() {
        // Check if music is enabled in preferences (default false)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!musicIsBound && preferences.getBoolean("music", false)) {
            musicService.resumeMusic();
            musicIsBound = true;
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPlay:
                Intent play = new Intent(this, InGame.class);
                startActivity(play);
                break;
            case R.id.buttonSettings:
                Log.d("MainMenu", "Settings pressed!");
                //Intent settings = new Intent(this, InGame.class);
                //startActivity(settings);
                break;
            case R.id.buttonMapTest:
                Intent mapTest = new Intent(this, MapTestActivity.class);
                startActivity(mapTest);
                break;
        }
    }
}
