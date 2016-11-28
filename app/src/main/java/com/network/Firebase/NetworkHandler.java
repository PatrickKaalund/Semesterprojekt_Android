package com.network.Firebase;

import android.provider.ContactsContract;
import android.util.Log;

import com.gamelogic.DataContainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public class NetworkHandler {

    private DatabaseReference mFirebaseDatabaseReference;
    private ArrayList<RemotePlayer> playerListeners;
    private String game;
    private boolean multiPlayerGame;
    private String mother;

    public enum gameStatus {
        CREATE_GAME,
        FILLING_GAME,
        BEGUN
    }

    public NetworkHandler(boolean multiPlayerGame) {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        playerListeners = new ArrayList<>();

        this.multiPlayerGame = multiPlayerGame;

        if (this.multiPlayerGame) {
            // testing - adding games
            mother = "Games";
            String testGame1 = "Game1";
            String testGame2 = "Game2";
            String testGame3 = "Game3";

            // Remove old data
            mFirebaseDatabaseReference.child(mother).removeValue();

            mFirebaseDatabaseReference.child(mother).child(testGame1).child("Status").setValue(gameStatus.BEGUN.ordinal());
            mFirebaseDatabaseReference.child(mother).child(testGame2).child("Status").setValue(gameStatus.BEGUN.ordinal());
            mFirebaseDatabaseReference.child(mother).child(testGame3).child("Status").setValue(gameStatus.FILLING_GAME.ordinal());

            Log.d("NetworkHandler", "NetworkHandler started");

            searchForGame();
        }
    }


    private void searchForGame() {
        mFirebaseDatabaseReference.child(mother).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object receivedObject = dataSnapshot.getValue();
                if (receivedObject != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(receivedObject.toString());
                        Log.d("NetworkHandler", "Received json: " + jsonObject.toString());

                        Iterator<?> keys = jsonObject.keys();

                        while( keys.hasNext() ) {
                            String key = (String)keys.next();
                            if ( jsonObject.get(key) instanceof JSONObject ) {
                                Log.d("NetworkHandler", "Key: " + key + " : game status: " + jsonObject.get(key).toString());
                                Integer gameStatus = ((JSONObject) jsonObject.get(key)).getInt("Status");
                                if (gameStatus == NetworkHandler.gameStatus.FILLING_GAME.ordinal()){
                                    Log.d("NetworkHandler", "Found game to join: " + key);
                                    DataContainer.player.setPlayerID(2);
                                    beginGame(key);
                                    startListenGame();
                                    return; // stop searching for game to join in iterator
                                }
                            }
                        }

                        // creating new game if all games are filled
                        /**
                         *          !!!!!!!!!!!!!!!! TO DO !!!!!!!!!!!!!!!
                         */

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

    public void addPlayerListener(RemotePlayer playerListener) {
        playerListeners.add(playerListener);
    }

    private void startListenGame() {
        mFirebaseDatabaseReference.child(mother).child(game).addValueEventListener(new ValueEventListener() {
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

                        for (RemotePlayer playerListener : playerListeners) {
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
        mFirebaseDatabaseReference.child(mother).child(game).child("Time").setValue(time);
        mFirebaseDatabaseReference.child(mother).child(game).child("X").setValue(centerX);
        mFirebaseDatabaseReference.child(mother).child(game).child("Y").setValue(centerY);
        mFirebaseDatabaseReference.child(mother).child(game).child("Angle").setValue(angle);
    }

    public void beginGame(String game) {
        this.game = game;
        updatePlayerPosition(DataContainer.player.getPos().x, DataContainer.player.getPos().y, 0);
        mFirebaseDatabaseReference.child(mother).child(game).child("Status").setValue(NetworkHandler.gameStatus.BEGUN.ordinal());

    }
}
