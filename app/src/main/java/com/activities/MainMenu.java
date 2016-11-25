package com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.patrickkaalund.semesterprojekt_android.R;
import com.network.Firebase.FirebaseActivity;
import com.network.MQTT.MQTTActivity;
import com.teststuff.MapTestActivity;

public class MainMenu extends BaseActivity implements View.OnClickListener {

    private Button mapTest, mqttTest, firebaseTest;
    private TextView play, settings, quit, debug;
    private GridLayout gridLayout;
    boolean firstRun = true;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

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
            //gridLayout.setVisibility(View.GONE);
            firstRun = false;
        } else {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    playClick();
                    Intent play = new Intent(this, InGame.class);
                    startActivity(play);
                    break;
                case R.id.buttonSettings:
                    playClick();
                    Intent settings = new Intent(this, OptionsActivity.class);
                    startActivity(settings);
                    break;
                case R.id.buttonQuit:
                    playClick();
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    break;
                case R.id.buttonDebug:
                    playClick();
                    findViewById(R.id.buttonMapTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonFirebaseTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonMQTTTest).setVisibility(View.VISIBLE);
                    break;
                case R.id.buttonMapTest:
                    playClick();
                    Intent mapTest = new Intent(this, MapTestActivity.class);
                    startActivity(mapTest);
                    break;
                case R.id.buttonMQTTTest:
                    playClick();
                    Intent mqttTest = new Intent(this, MQTTActivity.class);
                    startActivity(mqttTest);
                    break;
                case R.id.buttonFirebaseTest:
                    playClick();
                    Intent firebaseTest = new Intent(this, FirebaseActivity.class);
                    startActivity(firebaseTest);
                    break;
            }
        }
    }

    private void playClick() {
        if (preferences.getBoolean("sound", true)) {
            MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.click);
            mediaPlayer.start();
        }
    }
}
