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
        mediaPlayer = getMediaPlayer(context);
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


    // Create MediaPlayer with empty subtitle-controller to avoid LogCat crap - solution from Stackoverflow:
    // http://stackoverflow.com/questions/20087804/should-have-subtitle-controller-already-set-mediaplayer-error-android
    static MediaPlayer getMediaPlayer(Context context){

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName( "android.media.MediaTimeProvider" );
            Class<?> cSubtitleController = Class.forName( "android.media.SubtitleController" );
            Class<?> iSubtitleControllerAnchor = Class.forName( "android.media.SubtitleController$Anchor" );
            Class<?> iSubtitleControllerListener = Class.forName( "android.media.SubtitleController$Listener" );

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler() {
                    @Override
                    public void publish(LogRecord logRecord) {

                    }

                    @Override
                    public void flush() {

                    }

                    @Override
                    public void close() throws SecurityException {

                    }
                });
            }
            catch (IllegalAccessException e) {return mediaplayer;}
            finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {}

        return mediaplayer;
    }
}
