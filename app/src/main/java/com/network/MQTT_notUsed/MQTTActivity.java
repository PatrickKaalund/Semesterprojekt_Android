package com.network.MQTT_notUsed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.patrickkaalund.semesterprojekt_android.R;

//import org.eclipse.paho.android.service.MqttAndroidClient;
//import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
//import org.eclipse.paho.client.mqttv3.IMqttActionListener;
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.IMqttToken;
//import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTTActivity extends AppCompatActivity implements View.OnClickListener {

//    //private String server = "2.108.149.191";           // Christian IP
//    //private String server = "90.185.73.218";           // Thor IP
//    private String server = "192.168.0.2";               // Patrick local IP
//    private String port = "1883";
//    private String serverUri = "tcp://" + server + ":" + port;
//
//    private MqttAndroidClient mqttAndroidClient;
//
//    final String subscriptionTopic = "exampleAndroidTopic";
//    final String publishTopic = "exampleAndroidTopic";
//
//    private Button buttonPublish;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);

//        buttonPublish = (Button) findViewById(R.id.buttonMqttPublish);
//        buttonPublish.setOnClickListener(this);
//
//        // Debug info
//        Log.d("MQTTActivity", "Connecting to: " + serverUri);
//
//        mqttAndroidClient = new MqttAndroidClient(this.getApplicationContext(), serverUri, "Patricks ID :-)");
//
//        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//
//                if (reconnect) {
//                    Log.d("MQTTActivity", "Reconnected to : " + serverURI);
//                    // Because Clean Session is true, we need to re-subscribe
//                    //subscribeToTopic();
//                } else {
//                    Log.d("MQTTActivity", "Connected to: " + serverURI);
//                }
//            }
//
//            @Override
//            public void connectionLost(Throwable cause) {
//                Log.d("MQTTActivity", "The Connection was lost.");
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                long time = System.nanoTime() - Long.parseLong(new String(message.getPayload()));
//                //Log.d("MQTTActivity", "Incoming message: " + new String(message.getPayload()));
//                Log.d("MQTTActivity", "Incoming message time delay: " + time/1000000 + " millis");
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
//
//        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        mqttConnectOptions.setAutomaticReconnect(true);
//        mqttConnectOptions.setCleanSession(false);
//
//        try {
//            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
//                    disconnectedBufferOptions.setBufferEnabled(true);
//                    disconnectedBufferOptions.setBufferSize(100);
//                    disconnectedBufferOptions.setPersistBuffer(false);
//                    disconnectedBufferOptions.setDeleteOldestMessages(false);
//                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
//                    subscribeToTopic();
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Log.d("MQTTActivity", "Failed to connect to: " + serverUri + ". Exception: " + exception);
//                }
//            });
//        } catch (MqttException ex) {
//            Log.d("MQTTActivity", "Exception when connecting!");
//            ex.printStackTrace();
//        }
    }
//
//    public void subscribeToTopic() {
//        try {
//            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    Log.d("MQTTActivity", "Subscribed!");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//                    Log.d("MQTTActivity", "Failed to subscribe");
//                }
//            });
//
//        } catch (MqttException ex) {
//            System.err.println("Exception whilst subscribing");
//            ex.printStackTrace();
//        }
//    }
//
//    public void publishMessage() {
//
//        try {
//            MqttMessage message = new MqttMessage();
//            String publishMessage = String.valueOf(System.nanoTime());
//            message.setPayload(publishMessage.getBytes());
//            mqttAndroidClient.publish(publishTopic, message);
//            Log.d("MQTTActivity", "Message Published");
//            if (!mqttAndroidClient.isConnected()) {
//                Log.d("MQTTActivity", mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
//            }
//        } catch (MqttException e) {
//            System.err.println("Error Publishing: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMqttPublish:
//                publishMessage();
                break;
        }
    }
}