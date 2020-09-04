package com.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private static final String EXCHANGE_NAME = "FANOUT_EXCHANGE";

    public static void produce() throws IOException, TimeoutException {
        // 获得连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 获得新的连接
        Connection connection = connectionFactory.newConnection();
        // 获得信道
        Channel channel = connection.createChannel();
        // 声明一个 交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        String message = "Hello World!";
        // 向交换机发送一个消息（交换机名称，路由key，属性，消息Byte数组）
        channel.basicPublish(EXCHANGE_NAME,"red",null,message.getBytes());
        System.out.println("生产者生产: " + message);
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
       Producer.produce();
    }
}
