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

public class FirebaseActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mFirebaseDatabaseReference;
    private Button mSendButton;
    private EditText mMessageEditText;
    private TextView mMessageReceivedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        mSendButton = (Button) findViewById(R.id.button_send_message);
        mMessageEditText = (EditText) findViewById(R.id.text_message);
        mMessageReceivedText = (TextView) findViewById(R.id.text_console);

        mSendButton.setOnClickListener(this);

        mFirebaseDatabaseReference.child("Player").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object receivedObject = dataSnapshot.getValue();
                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
                        Log.d("FirebaseActivity", "Received json: " + jsonObject.toString());
                        Long receivedTime = jsonObject.getLong("Time");
                        long time = System.currentTimeMillis() - receivedTime;
                        mMessageReceivedText.setText("Time delay: " + time + " millis");
                        Log.d("FirebaseActivity", "Time delay: " + time + " millis");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("FirebaseActivity", "Cancel subscriebtion!");
            }
        });
    }

    public void sendMessage(String message) {
        Long time = System.currentTimeMillis();
        mFirebaseDatabaseReference.child("Player").child("Time").setValue(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_send_message:
                sendMessage("");
                break;
        }
    }
}