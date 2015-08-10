package com.github.pires.obd.reader.rabbitmq;

import android.os.AsyncTask;
import android.util.Log;

import com.github.pires.obd.reader.net.ObdReading;
import com.google.gson.Gson;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Philipp on 04.08.2015.
 */
public class RabbitMqManager {
    private final String QUEUE_NAME = "obdData";


    public RabbitMqManager() {
        setUpChannel();
    }

    private void setUpChannel() {


    }

    /**
     * Sends a message to the RabbitMQ
     *
     * @param message
     */
    public void sendMessage(String message) {
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                ConnectionFactory factory
                        = new ConnectionFactory();
                factory.setHost("");
                factory.setPassword("");
                factory.setUsername("");
                Connection connection = null;
                Channel channel;
                try {
                    connection = factory.newConnection();
                    channel = connection.createChannel();

                    for (String message : params) {

                        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                        System.out.println(" [x] Sent '" + message + "'");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(message);
    }

    public void close() {

    }

    public void sendMessage(ObdReading reading) {

        String message = new Gson().toJson(reading);
        sendMessage(message);
        Log.d("RabbitMQ", message);
    }
}
