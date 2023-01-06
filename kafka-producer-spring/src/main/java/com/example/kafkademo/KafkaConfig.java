package com.example.kafkademo;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfig {

    static Map<String, Object> config() {

        Map<String, Object> config = new HashMap<>();

        String kafkaHost = System.getenv("KAFKA_HOST");
        String rhoasClientID = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID");
        String rhoasClientSecret = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET");
        String rhoasOauthTokenUrl = System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL");

        config.put("bootstrap.servers", kafkaHost);

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "OAUTHBEARER");

        config.put("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required clientId=\"" + rhoasClientID + "\" clientSecret=\"" + rhoasClientSecret + "\" oauth.token.endpoint.uri=\"" + rhoasOauthTokenUrl + "\";");
        config.put("sasl.login.callback.handler.class", "org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler");
        config.put("sasl.oauthbearer.token.endpoint.url", rhoasOauthTokenUrl);
        config.put("sasl.oauthbearer.scope.claim.name", "api.iam.service_accounts");

        return config;
    }
}
