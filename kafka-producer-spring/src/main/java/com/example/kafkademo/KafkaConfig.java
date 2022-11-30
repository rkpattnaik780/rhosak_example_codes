package com.example.kafkademo;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {

    static Map<String, Object> config() {

        Map<String, Object> config = new HashMap<>();

        var kafkaClientId = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID");
        var kafkaClientSecret = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET");

        config.put("bootstrap.servers", System.getenv("KAFKA_HOST"));

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "OAUTHBEARER");

        config.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required clientId=\"" + kafkaClientId + "\" clientSecret=\"" + kafkaClientSecret + "\" oauth.token.endpoint.uri=\"" + System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL") + "\";");
        config.put("sasl.login.callback.handler.class", "org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler");
        config.put("sasl.oauthbearer.token.endpoint.url", System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL"));
        config.put("sasl.oauthbearer.scope.claim.name", "api.iam.service_accounts");

        return config;
    }
}
