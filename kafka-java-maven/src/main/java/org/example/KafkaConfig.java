package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaConfig {

    static Properties properties() {

        String KAFKA_HOST = System.getenv("KAFKA_HOST");
        String RHOAS_CLIENT_ID = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID");
        String RHOAS_CLIENT_SECRET = System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET");
        String RHOAS_OAUTH_TOKEN_URL = System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL");

        var properties= new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_HOST);
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "OAUTHBEARER");

        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required clientId=\"" + RHOAS_CLIENT_ID + "\" clientSecret=\"" + RHOAS_CLIENT_SECRET + "\" oauth.token.endpoint.uri=\"" + RHOAS_OAUTH_TOKEN_URL + "\";");

        properties.setProperty("sasl.login.callback.handler.class", "org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler");
        properties.setProperty("sasl.oauthbearer.token.endpoint.url", RHOAS_OAUTH_TOKEN_URL);
        properties.setProperty("sasl.oauthbearer.scope.claim.name", "api.iam.service_accounts");

        return properties;
    }
}
