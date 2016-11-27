package com.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.network.Firebase.FirebaseActivity;
import com.network.MQTT.MQTTActivity;
import com.teststuff.MapTestActivity;

public class MainMenu extends BaseActivity implements View.OnClickListener {

    private Button mapTest, mqttTest, firebaseTest;
    private TextView play, settings, quit, debug, playMulti;
    private final ThreadLocal<ProgressBar> progressBar = new ThreadLocal<>();
    private GridLayout masterTank;
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

        progressBar.set((ProgressBar) findViewById(R.id.progressBar));
        play = (TextView) findViewById(R.id.buttonPlay);
        playMulti = (TextView) findViewById(R.id.buttonPlayMulti);
        settings = (TextView) findViewById(R.id.buttonSettings);
        quit = (TextView) findViewById(R.id.buttonQuit);
        debug = (TextView) findViewById(R.id.buttonDebug);
        mapTest = (Button) findViewById(R.id.buttonMapTest);
        mqttTest = (Button) findViewById(R.id.buttonMQTTTest);
        firebaseTest = (Button) findViewById(R.id.buttonFirebaseTest);
        masterTank = (GridLayout) findViewById(R.id.MasterTank);

        play.setOnClickListener(this);
        playMulti.setOnClickListener(this);
        settings.setOnClickListener(this);
        quit.setOnClickListener(this);
        debug.setOnClickListener(this);
        mapTest.setOnClickListener(this);
        mqttTest.setOnClickListener(this);
        firebaseTest.setOnClickListener(this);
        masterTank.setOnClickListener(this);
    }

    @SuppressLint("NewApi")     // Supressing error.. Bug in Android  Issue 189041: setForeground() incorrectly flagged as requiring API 23 (NewApi) for ViewGroups extending FrameLayout
    protected void onPostResume() {
        // Remove dark overlay from screen
        findViewById(R.id.OuterRelativeLayout).setForeground(null);

        // Hide progressBar
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        // Set music track
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("track", R.raw.menu_music).apply();

        super.onPostResume();
    }

    @SuppressLint("NewApi")     // Supressing error.. Bug in Android  Issue 189041: setForeground() incorrectly flagged as requiring API 23 (NewApi) for ViewGroups extending FrameLayout
    public void onClick(View v) {
        if (firstRun) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RLayout);
            relativeLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.startup));
            relativeLayout.setVisibility(View.VISIBLE);
            audioPlayer.playAudioFromRaw(R.raw.baretta);
            firstRun = false;
        } else {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    // Put dark overlay on screen
                    findViewById(R.id.OuterRelativeLayout).setForeground(getDrawable(R.color.black_overlay));
                    progressBar.get().setVisibility(View.VISIBLE);
                    progressBar.get().bringToFront();
                    progressBar.get().invalidate();

                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent play = new Intent(this, InGame.class);
                    startActivity(play);
                    break;

                case R.id.buttonPlayMulti:
                    Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.buttonSettings:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent settings = new Intent(this, OptionsActivity.class);
                    startActivity(settings);
                    break;

                case R.id.buttonQuit:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    break;

                case R.id.buttonDebug:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    findViewById(R.id.buttonMapTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonFirebaseTest).setVisibility(View.VISIBLE);
                    findViewById(R.id.buttonMQTTTest).setVisibility(View.VISIBLE);
                    break;

                case R.id.buttonMapTest:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent mapTest = new Intent(this, MapTestActivity.class);
                    startActivity(mapTest);
                    break;

                case R.id.buttonMQTTTest:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent mqttTest = new Intent(this, MQTTActivity.class);
                    startActivity(mqttTest);
                    break;

                case R.id.buttonFirebaseTest:
                    v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));
                    audioPlayer.playAudioFromRaw(R.raw.click);
                    Intent firebaseTest = new Intent(this, FirebaseActivity.class);
                    startActivity(firebaseTest);
                    break;
            }
        }
    }
}
