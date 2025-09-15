package com.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
/**
 * Hello world!
 *
 */
public class Receiver2 {
	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
		    String message = new String(delivery.getBody(), "UTF-8");
		    System.out.println(" [x] Received '" + message + "'");
		};
		
		
		channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

	}
}
