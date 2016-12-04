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
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

class HighScoreObject {
    public int value;
    public String name;

    public HighScoreObject(String name, int value) {
        this.value = value;
        this.name = name;
    }
}

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

                Comparator<HighScoreObject> comparator = new HighScoreComparator();
                PriorityQueue<HighScoreObject> queue = new PriorityQueue<>(10, comparator);

                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
                        Iterator<?> keys = jsonObject.keys();

                        Log.e("NetworkHandler", "Received: " + jsonObject.toString());

                        // Add highscore objects to queue
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
//                            Log.d("NetworkHandler", "Key: " + key + ". Value is: " + jsonObject.get(key));
                            queue.offer(new HighScoreObject(key, (int) jsonObject.get(key)));
                        }

                        // Add sorted queue to string array
                        while (queue.peek() != null){
                            HighScoreObject highScoreObject = queue.poll();
//                            Log.e("NetworkHandler", "Sorted: " + highScoreObject.name + " " + highScoreObject.value);
                            highScoreObjects.add(highScoreObject.name + " : " + highScoreObject.value);
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


    // Sorting highest to lowest
    public class HighScoreComparator implements Comparator<HighScoreObject> {
        @Override
        public int compare(HighScoreObject lhs, HighScoreObject rhs) {
            return rhs.value - lhs.value;
        }
    }
}
