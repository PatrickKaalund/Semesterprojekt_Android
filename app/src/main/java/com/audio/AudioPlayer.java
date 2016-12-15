package com.audio;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

public class AudioPlayer {
    private MediaPlayer mediaPlayer;
    private Context context;
    private SharedPreferences preferences;

    public AudioPlayer(Context context) {
        this.context = context;
        mediaPlayer = new MediaPlayer();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void playAudioFromRaw(int raw) {

        // Check if sound is enabled
        if (preferences.getBoolean("sound", true)) {
            try {
                Uri rawPath = Uri.parse("android.resource://" + context.getPackageName() + "/" + raw);
                // Reset MediaPlayer and prepare Async
                mediaPlayer.reset();
                mediaPlayer.setDataSource(context, rawPath);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });

            } catch (IOException e) {
                Log.e("SHOOTER", "FAILED IN AUDIOPLAYER " + context.getResources().getResourceName(raw));
            }
        }
    }
}
