package com.network.ZeroMQ;

import android.app.Activity;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.patrickkaalund.semesterprojekt_android.R;

import java.util.Date;

public class ZeroMQActivity extends Activity {
   /* private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.fragment_multiplayer.activity_zero_mq);

        textView = (TextView)findViewById(R.id.text_console);
        editText = (EditText)findViewById(R.id.text_message);

        new Thread(new ZeroMQServer(serverMessageHandler)).start();

        findViewById(R.id.button_send_message).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ZeroMQMessageTask(clientMessageHandler).execute(getTaskInput());
                    }

                    protected String getTaskInput() {
                        return editText.getText().toString();
                    }
                });
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

    private static String getTimeString() {
        return DATE_FORMAT.format(new Date());
    }

    private void serverMessageReceived(String messageBody) {
        // clear text
        textView.setText("");
        textView.append(getTimeString() + " - server received: " + messageBody + "\n");
    }

    private void clientMessageReceived(String messageBody) {
        textView.append(getTimeString() + " - client received: " + messageBody + "\n");
    }

    private final MessageListenerHandler serverMessageHandler = new MessageListenerHandler(
            new IMessageListener() {
                @Override
                public void messageReceived(String messageBody) {
                    serverMessageReceived(messageBody);
                }
            },
            Util.MESSAGE_PAYLOAD_KEY);

    private final MessageListenerHandler clientMessageHandler = new MessageListenerHandler(
            new IMessageListener() {
                @Override
                public void messageReceived(String messageBody) {
                    clientMessageReceived(messageBody);
                }
            },
            Util.MESSAGE_PAYLOAD_KEY);*/
}

