package activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.patrickkaalund.semesterprojekt_android.R;
import services.MusicService;

public class MainMenu extends AppCompatActivity {
    boolean musicIsBound = false;
    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Add music boolean to preference manager if not present
        if (!preferences.contains("music")) {
            preferences.edit()
                    .putBoolean("music", false) // Music disabled by default
                    .apply();
        }
        //preferences.edit().putBoolean("music", true).apply();     // Enable music (debug)
        doBindService();
    }

    // Kunne undlades..
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_bar_options_main, menu);
        return true;
    }

    // Kunne undlades..
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.options) {
            // Todo Implement options
            Toast.makeText(this, "Not implemented", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.quit)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    // Handle music when Home is pressed
    @Override
    protected void onPause() {
        if(musicIsBound) {
            musicService.pauseMusic();
            musicIsBound = false;
        }
        super.onPause();
    }

    // Handle music when app is resumed
    @Override
    protected void onPostResume() {
        // Check if music is enabled in preferences (default false)
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!musicIsBound && preferences.getBoolean("music", false)) {
            musicService.resumeMusic();
            musicIsBound = true;
        }
        super.onPostResume();
    }

    ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this,MusicService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
        musicIsBound = true;
    }

    void doUnbindService() {
        if(musicIsBound) {
            unbindService(serviceConnection);
            musicIsBound = false;
        }
    }
}
