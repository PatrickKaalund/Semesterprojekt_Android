package com.network.Firebase;

/**
 * Created by PatrickKaalund on 13/11/2016.
 */

public interface RemotePlayer {

    //    public abstract void addPlayerListener();
//    public abstract void receiveMessage(String received);

    public abstract void updatePlayerPosition(float centerX, float centerY, int angle);
}
