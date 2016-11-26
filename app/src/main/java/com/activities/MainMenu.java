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

import com.audio.AudioPlayer;
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
    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        audioPlayer = new AudioPlayer(this);

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

    @Override
    public void onClick(View v) {
        if (firstRun) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RLayout);
            relativeLayout.setVisibility(View.VISIBLE);
            firstRun = false;
        } else {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent play = new Intent(this, InGame.class);
                    startActivity(play);
                    break;
                case R.id.buttonSettings:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent settings = new Intent(this, OptionsActivity.class);
                    startActivity(settings);
                    break;
                case R.id.buttonQuit:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    break;
                case R.id.buttonDebug:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    findViewById(R.id.buttonMapTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonFirebaseTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonMQTTTest).setVisibility(View.VISIBLE);
                    break;
                case R.id.buttonMapTest:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent mapTest = new Intent(this, MapTestActivity.class);
                    startActivity(mapTest);
                    break;
                case R.id.buttonMQTTTest:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent mqttTest = new Intent(this, MQTTActivity.class);
                    startActivity(mqttTest);
                    break;
                case R.id.buttonFirebaseTest:
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent firebaseTest = new Intent(this, FirebaseActivity.class);
                    startActivity(firebaseTest);
                    break;
            }
        }
    }
}
