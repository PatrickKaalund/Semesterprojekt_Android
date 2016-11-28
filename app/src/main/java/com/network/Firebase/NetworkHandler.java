package com.network.Firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public class NetworkHandler {

    private DatabaseReference mFirebaseDatabaseReference;
    private ArrayList<RemotePlayer> playerListeners;
    private String game;

    public NetworkHandler(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        playerListeners = new ArrayList<>();

        game = "Game";

        // Create game on firebase, or empties an existing game for testing :-)
        mFirebaseDatabaseReference.child(game).removeValue();

        Log.d("NetworkHandler", "NetworkHandler started");

        startListenOnFirebase();
    }

    public void addPlayerListener(RemotePlayer playerListener){
        playerListeners.add(playerListener);
    }

    private void startListenOnFirebase(){
        mFirebaseDatabaseReference.child(game).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object receivedObject = dataSnapshot.getValue();
                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
//                        Log.d("NetworkHandler", "Received json: " + jsonObject.toString());
                        Long receivedTime = jsonObject.getLong("Time");
                        Long xPos = jsonObject.getLong("X");
                        Long yPos = jsonObject.getLong("Y");
                        int angle = jsonObject.getInt("Angle");
                        long time = System.currentTimeMillis() - receivedTime;
//                        mMessageReceivedText.setText("Time delay: " + time + " millis");
//                        Log.d("NetworkHandler", "Time delay: " + time + " millis");

                        for(RemotePlayer playerListener : playerListeners){
                            playerListener.updatePlayerPosition(xPos, yPos, angle);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("NetworkHandler", "Cancel subscriebtion!");
            }
        });
    }

    public void updatePlayerPosition(float centerX, float centerY, int angle) {
//        Log.d("NetworkHandler", "Updating player position on firebase: " + centerX + ", " + centerY + ". Angle: " + angle);
        Long time = System.currentTimeMillis();
        mFirebaseDatabaseReference.child(game).child("Time").setValue(time);
        mFirebaseDatabaseReference.child(game).child("X").setValue(centerX);
        mFirebaseDatabaseReference.child(game).child("Y").setValue(centerY);
        mFirebaseDatabaseReference.child(game).child("Angle").setValue(angle);
    }
}
