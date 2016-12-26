package com.example;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JmsConsumerApplication {
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Value("${demo.queue-name}")
	private String queueName;
	
	@Value("${demo.queue-json-name}")
	private String queueJsonName;
	
	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}
	
	@Bean
	Queue jsonQueue() {
		return new Queue(queueJsonName, false);
	}
	
    @Bean
    Receiver receiver() {
        return new Receiver();
    }
    
    @Bean
    JsonReceiver jsonReceiver() {
        return new JsonReceiver();
    }
    
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}
	
	@Bean
	SimpleMessageListenerContainer jsonContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(queueJsonName);
		container.setMessageListener(jsonListenerAdapter(jsonReceiver()));
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
	@Bean
	MessageListenerAdapter jsonListenerAdapter(JsonReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(JmsConsumerApplication.class, args);
		
		
	}
}
