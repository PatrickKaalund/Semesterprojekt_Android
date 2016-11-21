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
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.network.Firebase.FirebaseActivity;
import com.network.MQTT.MQTTActivity;
import com.teststuff.MapTestActivity;

public class MainMenu extends BaseActivity implements View.OnClickListener {

    Button mapTest, mqttTest, firebaseTest;
    TextView play, settings, quit, debug;
    GridLayout gridLayout;
    boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        play = (TextView) findViewById(R.id.buttonPlay);
        settings = (TextView) findViewById(R.id.buttonSettings);
        quit = (TextView) findViewById(R.id.buttonQuit);
        debug = (TextView) findViewById(R.id.buttonDebug);
        mapTest = (Button) findViewById(R.id.buttonMapTest);
        mqttTest = (Button) findViewById(R.id.buttonMQTTTest);
        firebaseTest = (Button) findViewById(R.id.buttonFirebaseTest);
        gridLayout = (GridLayout) findViewById(R.id.MasterTank);

        play.setOnClickListener(this);
        settings.setOnClickListener(this);
        quit.setOnClickListener(this);
        debug.setOnClickListener(this);
        mapTest.setOnClickListener(this);
        mqttTest.setOnClickListener(this);
        firebaseTest.setOnClickListener(this);
        gridLayout.setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("window", R.raw.menu_music).apply();

        super.onPostResume();
    }

/*    @Override
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
    }*/


    @Override
    public void onClick(View v) {
        if (firstRun) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RLayout);
            relativeLayout.setVisibility(View.VISIBLE);
            gridLayout.setVisibility(View.GONE);
            firstRun = false;
        } else {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    Intent play = new Intent(this, InGame.class);
                    startActivity(play);
                    break;
                case R.id.buttonSettings:
                    Intent settings = new Intent(this, OptionsActivity.class);
                    startActivity(settings);
                    break;
                case R.id.buttonQuit:
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    break;
                case R.id.buttonDebug:
                    findViewById(R.id.buttonMapTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonFirebaseTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonMQTTTest).setVisibility(View.VISIBLE);
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
}
