package com.network.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activities.InGame;
import com.example.patrickkaalund.semesterprojekt_android.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Patrick on 11/01/16.
 */

public class FirebaseActivity extends AppCompatActivity /*implements View.OnClickListener, RemotePlayer */{

    private Button mSendButton;
    private EditText mMessageEditText;
    private TextView mMessageReceivedText;
    private NetworkHandler networkHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
    }
/*
        networkHandler = new NetworkHandler();
        networkHandler.addPlayerListener(this);

        mSendButton = (Button) findViewById(R.id.button_send_message);
        mMessageEditText = (EditText) findViewById(R.id.text_message);
        mMessageReceivedText = (TextView) findViewById(R.id.text_console);

        mSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_message:
                networkHandler.updatePlayerPosition(200,200);
                break;
        }
    }

    @Override
    public void receiveMessage(String received) {
        Log.d("FirebaseActivity", "Received: " + received);
    }*/
}