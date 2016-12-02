package com.activities;

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
import com.fragments.ButtonMainFragment;
import com.fragments.EndScreenFragment;
import com.fragments.LoginFragment;
import com.fragments.MultiplayerFragment;
import com.views.DropDownMenu;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class InGame extends BaseActivity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = new Game(this);

        setContentView(game.getGameView());

        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.activity_in_game, null),
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        game.setJoystick((JoystickView) findViewById(R.id.joystickView));
        game.setShootButton((FloatingActionButton) findViewById(R.id.floatingActionButton));
        game.setInventoryButton((DropDownMenu) findViewById(R.id.dropdown_menu));
        Log.d("IndGame", "onCreate");

        //endGame();
    }

    @Override
    protected void onPause() {
        game.gamePause();
        super.onPause();
        Log.d("IndGame", "onPause");
    }

    @Override
    protected void onPostResume() {
        game.gameStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt("track", R.raw.challenge_mode).apply();

        super.onPostResume();
        Log.d("IndGame", "onPostResume");
    }

    @Override
    protected void onDestroy() {
        game.gameStop();
        super.onPause();
        Log.d("IndGame", "onDestroy");
    }

    public void endGame() {

        // Add red overlay to screen
        findViewById(R.id.relative_layout_endgame).setVisibility(View.VISIBLE);
        findViewById(R.id.relative_layout_endgame).bringToFront();

        Fragment fragment = new EndScreenFragment();
        game.gamePause();
        getSupportFragmentManager().beginTransaction()
            .add(R.id.fragment_endgame_holder, fragment)
            .commit();

    }
}
