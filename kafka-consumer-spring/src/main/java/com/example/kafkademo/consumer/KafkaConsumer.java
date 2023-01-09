package com.example.kafkademo.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "prices",
            containerFactory = "concurrentKafkaListenerContainerFactory",
            groupId = "group_id")

    // Method
    public void consume(String message)
    {
        // Print statement
        System.out.println("message = " + message);
    }
}
