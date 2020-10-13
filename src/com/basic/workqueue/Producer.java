package com.basic.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 探究默认交换机下的多消费者消费情况
 * preFetchCount
 * autoAck
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
        // 放入50个message
        String message = "Hello World!";
        for (int i = 0; i < 50; i++) {
            channel.basicPublish("",QUEUE_NAME,null,(message + i).getBytes());
            // 输出这个message
            System.out.println("生产者成功放入" + message + i);
        }
        channel.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        com.basic.workqueue.Producer.produce();
    }
}
