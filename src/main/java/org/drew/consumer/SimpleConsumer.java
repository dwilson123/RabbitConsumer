package org.drew.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * Created by wilsona on 4/12/16.
 */
public class SimpleConsumer {


    private final static String QUEUE_NAME = "Drew_Q";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("RabbitExchange", "topic");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, "RabbitExchange", "#");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
