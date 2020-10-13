package com.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author lqk
 * @Date 2020/10/13
 * @Description
 */
public class ConsumerConfirm {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        final String exchange_name = "confirm_exchange_test";
        final String queue_name = "confirm_exchange_test";
        final String routingKey = "confirm";
        channel.exchangeDeclare(exchange_name, "direct", false, false, null);
        channel.queueDeclare(queue_name, false, false, false, null);
        channel.queueBind(exchange_name, queue_name, routingKey);


        Consumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println("消费者收到消息: " + message);
            }
        };

        channel.basicConsume(queue_name, true, defaultConsumer);

        //channel.close();
        //connection.close();
    }
}
