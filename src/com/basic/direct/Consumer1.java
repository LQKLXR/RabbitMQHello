package com.basic.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {

    private static final String EXCHANGE_NAME = "DIRECT_EXCHANGE";
    private static final String QUEUE_NAME = "CONSUMER1";
    public static void consume() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 获得一个连接
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 信道声明交换机，即连接到 生产者当时放的那个交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        // 信道声明一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 把队列绑定到交换机上，绑定的路由key为"red"
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"red");
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
        Consumer1.consume();
    }
}
