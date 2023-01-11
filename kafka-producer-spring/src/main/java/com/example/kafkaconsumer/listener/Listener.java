package com.example.kafkaconsumer.listener;

import com.example.kafkaconfig.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Listener {

    @KafkaListener(topics = "prices",
            containerFactory = "concurrentKafkaListenerContainerFactory",
            groupId = "group_id")

    // Method
    public void consume(String message)
    {
        // Print statement
        System.out.println(message);
    }
}
