package com.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author lqk
 * @Date 2020/10/13
 * @Description
 */
public class ProducerConfirm {



    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        // 打开确认机制
        channel.confirmSelect();

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("确认成功   序号: " + deliveryTag + " , 是否多个: " + multiple);
                // 业务逻辑
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("确认失败   序号: " + deliveryTag + " , 是否多个: " + multiple);
                // 业务逻辑
            }
        });
        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Return消息: ");
                System.out.println("回复码:" + replyCode);
                System.out.println("交换机:" + exchange);
                System.out.println("routingKey:" + routingKey);
                System.out.println("数据:" + new String(body));
            }
        });


        final String exchange_name = "confirm_exchange_test";
        final String queue_name = "confirm_exchange_test";
        final String routingKey = "confirm";
        channel.exchangeDeclare(exchange_name, "direct", false, false, null);
        channel.queueDeclare(queue_name, false, false, false, null);
        channel.queueBind(exchange_name, queue_name, routingKey);
        // push几个正常的数据
        for (int i = 0; i < 10; i++) {
            channel.basicPublish(exchange_name, "confirm", null, ("正常数据" + i).getBytes());
        }
        // 再push几个routingKey不存在的数据

        channel.basicPublish(exchange_name, "notFound", true, null, "不正常数据".getBytes());


        /*channel.close();
        connection.close();*/


    }

}
