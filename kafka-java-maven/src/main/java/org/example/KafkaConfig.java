package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaConfig {

    static Properties properties() {
        var properties= new Properties();

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("KAFKA_HOST"));
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "OAUTHBEARER");

        properties.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.oauthbearer.OAuthBearerLoginModule required clientId=\"" + System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID") + "\" clientSecret=\"" + System.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET") + "\" oauth.token.endpoint.uri=\"" + System.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL") + "\";");

        properties.setProperty("sasl.login.callback.handler.class", "org.apache.kafka.common.security.oauthbearer.secured.OAuthBearerLoginCallbackHandler");
        properties.setProperty("sasl.oauthbearer.token.endpoint.url", "https://sso.redhat.com/auth/realms/redhat-external/protocol/openid-connect/token");
        properties.setProperty("sasl.oauthbearer.scope.claim.name", "api.iam.service_accounts");

        return properties;
    }
}
