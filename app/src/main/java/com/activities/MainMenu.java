package com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.audio.AudioPlayer;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.gamelogic.DataContainer;
import com.network.Firebase.FirebaseActivity;
import com.network.MQTT.MQTTActivity;
import com.teststuff.MapTestActivity;

public class MainMenu extends BaseActivity implements View.OnClickListener {

    boolean firstRun = true;
    private AudioPlayer audioPlayer;
    private RelativeLayout masterTank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        audioPlayer = new AudioPlayer(this);

        setContentView(R.layout.activity_main_menu);

        TextView play = (TextView) findViewById(R.id.buttonPlay);
        TextView playMulti = (TextView) findViewById(R.id.buttonPlayMulti);
        TextView settings = (TextView) findViewById(R.id.buttonSettings);
        TextView quit = (TextView) findViewById(R.id.buttonQuit);
        TextView debug = (TextView) findViewById(R.id.buttonDebug);
        Button mapTest = (Button) findViewById(R.id.buttonMapTest);
        Button mqttTest = (Button) findViewById(R.id.buttonMQTTTest);
        Button firebaseTest = (Button) findViewById(R.id.buttonFirebaseTest);
        masterTank = (RelativeLayout) findViewById(R.id.activity_main_menu);

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

    protected void onPostResume() {
        // Remove dark overlay from screen
        findViewById(R.id.overlay).setVisibility(View.INVISIBLE);

        // Hide progressBar
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        // Set music track
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("track", R.raw.menu_music).apply();

        super.onPostResume();
    }

    public void onClick(View v) {
        if (firstRun) {
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.RLayout);
            relativeLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.startup));
            relativeLayout.setVisibility(View.VISIBLE);
            audioPlayer.playAudioFromRaw(R.raw.baretta);
            masterTank.setOnClickListener(null);
            firstRun = false;
        } else {
            switch (v.getId()) {
                case R.id.buttonPlay:
                    play(v);
                    DataContainer.instance.multiplayerGame = false;
                    break;

                case R.id.buttonPlayMulti:
                    play(v);
                    DataContainer.instance.multiplayerGame = true;
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

    public void play(View v){
        // Add dark overlay to screen
        findViewById(R.id.overlay).setVisibility(View.VISIBLE);
        findViewById(R.id.overlay).bringToFront();

        // Bring progressBar to top
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        findViewById(R.id.progressBar).bringToFront();
        findViewById(R.id.progressBar).invalidate();

        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_clicked));

        audioPlayer.playAudioFromRaw(R.raw.click);
        Intent play = new Intent(this, InGame.class);
        startActivity(play);
    }
}
