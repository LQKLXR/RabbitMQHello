package com.basic.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 默认交换机下的直连模式-生产者
 */
public class Producer {

    private static final String QUEUE_NAME = "HELLO";

    public static void produce() throws IOException, TimeoutException {
        // 获得连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置 Host 和 Post
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        // 获得新的连接
        Connection connection = connectionFactory.newConnection();
        // 获得信道
        Channel channel = connection.createChannel();
        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 放入一个message
        String message = "Hello World!";
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        // 输出这个message
        System.out.println("生产者成功放入" + message);
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        Producer.produce();
    }
}
