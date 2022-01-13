package com.example.basiccrud.rmq;

import com.example.basiccrud.dtos.EmployeeDto;
import com.example.basiccrud.dtos.RmqUpdateDto;
import com.example.basiccrud.service.EmployeeService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RmqReceiver {

    @Autowired
    private EmployeeService employeeService;

    static final String queueName = "spring-boot";
    static final String topicExchangeName = "spring-boot-exchange";
    static final String groupIdName = "group-01";

    public RmqReceiver(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*@RabbitListener(queues = queueName)
    public void receiveRmqMessage(RmqUpdateDto message) {
        try {
            employeeService.updateEmployee(message.getEmployeeDto(), message.getSearchId());
            System.out.println("Received <" + message + "> via Rmq");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }*/

    @KafkaListener(topics = topicExchangeName, groupId = groupIdName, containerFactory = "kafkaListenerContainerFactory")
    public void receiveKafkaMessage(RmqUpdateDto message) {
        System.out.println("rmq dto received: " + message);
        try {
            employeeService.updateEmployee(message.getEmployeeDto(), message.getSearchId());
            System.out.println("Received <" + message + "> via Kafka");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
