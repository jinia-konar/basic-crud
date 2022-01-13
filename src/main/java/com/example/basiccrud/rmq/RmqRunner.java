/*package com.example.basiccrud.rmq;

import java.util.concurrent.TimeUnit;

import com.example.basiccrud.BasicCrudApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RmqRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final RmqReceiver receiver;
    static final String topicExchangeName = "spring-boot-exchange";

    public RmqRunner(RmqReceiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
        //receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }

}*/
