package com.graphics;

import android.util.Log;

/**
 * Created on 10/27/16.
 */

// for debugging
public class FPSMeasuring extends Thread {
    public static int counter = 0;
    public int latestFPS = 0;
    private boolean isRunning = true;

    // one time every second
    public void run() {
        while (isRunning) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("FPS counter", "FPS--> " + counter);
            latestFPS = counter;
            counter = 0;
        }
    }

    public void startFPS(){
        isRunning = true;
    }

    public void stopFPS(){
        isRunning = false;
    }
}