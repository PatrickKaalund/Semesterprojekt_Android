package com.network.Firebase;

import android.util.Log;

import com.fragments.LoginFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class LoginHandler {

    private DatabaseReference mFirebaseDatabaseReference;
    private String playerLocation;

    public LoginHandler() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        playerLocation = "Players";
    }

    public void login(final LoginFragment loginFragment, final String playerName) {

        // get player names on server
        mFirebaseDatabaseReference.child(playerLocation).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object receivedObject = dataSnapshot.getValue();

                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
//                        Log.d("NetworkHandler", "Received json: " + jsonObject.toString());

                        Iterator<?> keys = jsonObject.keys();

                        // check if this player name exist on server
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
//                            Log.d("NetworkHandler", "Key: " + key);

                            if(playerName.equals(key)){
//                                Log.e("NetworkHandler", "Already a player with this name: " + key);
                                loginFragment.loggedIn(false);
                                return; // stop searching
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                Log.e("NetworkHandler", "Player logged in: " + playerName);
                mFirebaseDatabaseReference.child(playerLocation).child(playerName).setValue(true);
                loginFragment.loggedIn(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("NetworkHandler", "Cancel subscription!");
            }
        });
    }
}
