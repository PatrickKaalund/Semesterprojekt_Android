package com.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.Game;
import com.example.patrickkaalund.semesterprojekt_android.R;
import com.fragments.EndScreenFragment;
import com.views.DropDownMenu;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class InGame extends BaseActivity {
    private Game game;
    private InGameBroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create instance of InGameBroadcastReceiver and register receiver
        receiver = new InGameBroadcastReceiver();
        this.registerReceiver(receiver, new IntentFilter(InGameBroadcastReceiver.ACTION));

        // Create instance of Game & set GameView as ContentView
        game = new Game(this);
        setContentView(game.getGameView());

        // inflate activity_in_game and put on top of GameView
        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.activity_in_game, null),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // Register views in Control object
        game.setJoystick((JoystickView) findViewById(R.id.joystickView));
        game.setShootButton((FloatingActionButton) findViewById(R.id.floatingActionButton));
        game.setInventoryButton((DropDownMenu) findViewById(R.id.dropdown_menu));

        Log.d("InGame", "onCreate");
    }

    @Override
    protected void onPause() {
        // Pause game clock
        game.gamePause();
        super.onPause();
        Log.d("InGame", "onPause");
    }

    @Override
    protected void onPostResume() {
        // Start game clock
        game.gameStart();

        // Change music to game music
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("track", R.raw.challenge_mode).apply();

        super.onPostResume();
        Log.d("InGame", "onPostResume");
    }

    @Override
    protected void onDestroy() {
        // Stop game clock and unregister InGameBroadcastReceiver
        game.gameStop();
        super.onPause();
        this.unregisterReceiver(receiver);
        Log.d("InGame", "onDestroy");
    }

    public void endGame(int shots, int hits, int kills) {

        // Add red overlay to screen
        findViewById(R.id.relative_layout_endgame).setVisibility(View.VISIBLE);
        findViewById(R.id.relative_layout_endgame).bringToFront();

        Fragment fragment = new EndScreenFragment();

        // Create Bundle with player stats
        Bundle bundle = new Bundle();
        bundle.putInt("shots", shots);
        bundle.putInt("hits", hits);
        bundle.putInt("kills", kills);
        fragment.setArguments(bundle);

        // Start EndScreenFragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_endgame_holder, fragment)
                .commit();
    }

    public class InGameBroadcastReceiver extends BroadcastReceiver {
        // Dummy string - we have only 1 call to this receiver..
        public static final String ACTION = "CALL_ME!";

        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");

            // If data is valid get stats from Player and stop game
            if (data != null && data.equals("KILL")) {
                int shots = intent.getIntExtra("shots", 0);
                int hits = intent.getIntExtra("hits", 0);
                int kills = intent.getIntExtra("kills", 0);

                Log.d("INGAME LISTENER", "Received message: " + data);
                Log.d("INGAME LISTENER", "Shots fired: " + shots);
                Log.d("INGAME LISTENER", "Hits: " + hits);
                Log.d("INGAME LISTENER", "Kills: " + kills);

                // Start end screen
                endGame(shots, hits, kills);
            }
        }
    }
}