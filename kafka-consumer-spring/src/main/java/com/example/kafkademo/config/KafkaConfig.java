package com.example.kafkademo.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

// Annotations
@EnableKafka
@Configuration

public class KafkaConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();

        var kafkaClientId = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID");
        var kafkaClientSecret = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET")

        config.put("bootstrap.servers", System.getenv("KAFKA_HOST"));

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "OAUTHBEARER");

        config.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required clientId=\"" + kafkaClientId + "\" clientSecret=\"" + kafkaClientSecret + "\" oauth.token.endpoint.uri=\"" + System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL") + "\";");
        config.put("sasl.login.callback.handler.class", "org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler");
        config.put("sasl.oauthbearer.token.endpoint.url", System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL"));
        config.put("sasl.oauthbearer.scope.claim.name", "api.iam.service_accounts");

        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<
                String, String> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


}
