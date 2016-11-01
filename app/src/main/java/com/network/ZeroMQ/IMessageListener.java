package com.network.ZeroMQ;

/**
 * Created by PatrickKaalund on 27/10/2016.
 */

public interface IMessageListener {
    void messageReceived(String messageBody);
}