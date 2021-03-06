package com.basic.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 探究默认交换机下的多消费者消费情况
 * preFetchCount
 * autoAck
 */
public class Consumer2 {

    private static final String QUEUE_NAME = "HELLO";

    public static void consume() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody());
                System.out.println("消费者消费" + message);
                // 模拟一些耗时操作，睡0.2秒
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 手动确认
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        // 设置自动确认为 false
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, (CancelCallback) null);
    }
    public static void main(String[] args) throws IOException, TimeoutException {
        Consumer2.consume();
    }
}
