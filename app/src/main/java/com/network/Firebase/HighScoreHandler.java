package com.network.Firebase;

import android.util.Log;

import com.fragments.HighScoreFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class HighScoreHandler {

    private DatabaseReference mFirebaseDatabaseReference;
    private String highScoreLocation;


    public HighScoreHandler() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        this.highScoreLocation = "HighScore";
    }

    public void requestHighScoreList(final HighScoreFragment highScoreFragment) {
        // get high score list from server
        mFirebaseDatabaseReference.child(highScoreLocation).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object receivedObject = dataSnapshot.getValue();

                ArrayList<String> highScoreObjects = new ArrayList<String>();
                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
                        Iterator<?> keys = jsonObject.keys();

                        // Add highscore objects
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
//                            Log.d("NetworkHandler", "Key: " + key + ". Value is: " + jsonObject.get(key));
                            highScoreObjects.add(key + " : " + jsonObject.get(key));
                        }

                        highScoreFragment.fillHighScore(highScoreObjects);
                        return;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                highScoreObjects.add("HighScore not available");
                highScoreFragment.fillHighScore(highScoreObjects);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("NetworkHandler", "Cancel subscription!");
            }
        });
    }
}
