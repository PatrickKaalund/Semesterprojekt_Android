package com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.network.Firebase.FirebaseActivity;
import com.network.MQTT.MQTTActivity;
import com.teststuff.MapTestActivity;

public class MainMenu extends BaseActivity implements View.OnClickListener {

    Button play, settings, mapTest, mqttTest, firebaseTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        play = (Button) findViewById(R.id.buttonPlay);
        settings = (Button) findViewById(R.id.buttonSettings);
        mapTest = (Button) findViewById(R.id.buttonMapTest);
        mqttTest = (Button) findViewById(R.id.buttonMQTTTest);
        firebaseTest = (Button) findViewById(R.id.buttonFirebaseTest);

        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        mapTest.setOnClickListener(this);
        mqttTest.setOnClickListener(this);
        firebaseTest.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("window", R.raw.menu_music).apply();

        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_bar_options_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options) {
            Intent settings = new Intent(this, OptionsActivity.class);
            startActivity(settings);
        } else if (item.getItemId() == R.id.quit)
            finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonPlay:
                Intent play = new Intent(this, InGame.class);
                startActivity(play);
                break;
            case R.id.buttonSettings:
                Intent settings = new Intent(this, OptionsActivity.class);
                startActivity(settings);
                break;
            case R.id.buttonMapTest:
                Intent mapTest = new Intent(this, MapTestActivity.class);
                startActivity(mapTest);
                break;
            case R.id.buttonMQTTTest:
                Intent mqttTest = new Intent(this, MQTTActivity.class);
                startActivity(mqttTest);
                break;
            case R.id.buttonFirebaseTest:
                Intent firebaseTest = new Intent(this, FirebaseActivity.class);
                startActivity(firebaseTest);
                break;
        }
    }
}
