package com.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.patrickkaalund.semesterprojekt_android.R;

import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnErrorListener {

    private final IBinder mediaBinder;
    private MediaPlayer mediaPlayer;

    public MusicService() {
        mediaBinder = new ServiceBinder();
    }

    public class ServiceBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mediaBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createMusicPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } finally {
                mediaPlayer = null;
            }
        }
    }

    void createMusicPlayer() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mediaPlayer = MediaPlayer.create(this, preferences.getInt("track", R.raw.menu_music));
        mediaPlayer.setOnErrorListener(this);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);
            mediaPlayer.setVolume(0.4f, 0.4f);
        }
        if (preferences.getBoolean("music", true))
            mediaPlayer.start();

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                onError(mediaPlayer, what, extra);
                return true;
            }
        });
    }

    public void pauseMusic() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public void resumeMusic() {
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }


    public boolean onError(MediaPlayer mp, int what, int extra) {

        Log.d("ERROR", "Music player failed");

        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } finally {
                mediaPlayer = null;
            }
        }
        return false;
    }

}
