package com.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private Context context;
    private SharedPreferences preferences;

    public AudioPlayer(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void playAudioFromRaw(int raw) {

        // Check if sound is enabled
        if (preferences.getBoolean("sound", true)) {
            try {
                Uri rawPath = Uri.parse("android.resource://" + context.getPackageName() + "/" + raw);
                // Create MediaPlayer and prepare Async
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(context, rawPath);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.reset();
                        mp.release();
                    }
                });

            } catch (IOException e) {
                Log.e("SHOOTER", "FAILED IN AUDIOPLAYER " + context.getResources().getResourceName(raw));
            }
        }
    }
}
