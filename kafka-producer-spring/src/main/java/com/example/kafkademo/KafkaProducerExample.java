package com.example.kafkademo;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import com.example.kafkaconfig.KafkaConfig;

import java.util.Map;
import java.lang.System;

@SpringBootApplication
public class KafkaProducerExample implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(KafkaProducerExample.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.exit(0);
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> config = KafkaConfig.Config();

		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return new DefaultKafkaProducerFactory<String, String>(config);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> template) {
		return args -> {
			template.send("prices", "Test Message");
		};
	}

}
