package com.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final String QUEUE_NAME = "HELLO";

    public static void consume() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody());
                System.out.println("消费者消费" + message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (CancelCallback) null);


    }
    public static void main(String[] args) throws IOException, TimeoutException {
        Consumer.consume();
    }
}
