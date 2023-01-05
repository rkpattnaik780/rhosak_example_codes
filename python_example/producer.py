import time
import os
from confluent_kafka import Producer

import requests

from dotenv import load_dotenv

load_dotenv()

kafka_host = os.getenv("KAFKA_HOST")
rhoas_client_id = os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID")
rhoas_client_secret = os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET")
rhoas_oauth_token_url = os.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL")


def _get_token(config):
    payload = {"grant_type": "client_credentials", "scope": "api.iam.service_accounts"}
    resp = requests.post(
        rhoas_oauth_token_url,
        auth=(rhoas_client_id, rhoas_client_secret),
        data=payload,
    )
    token = resp.json()
    return token["access_token"], time.time() + float(token["expires_in"])

topic = "prices"
producer_conf = {
    "bootstrap.servers": kafka_host,
    "security.protocol": "SASL_SSL",
    "sasl.mechanisms": "OAUTHBEARER",
    "oauth_cb": _get_token,
}
producer = Producer(producer_conf)

producer.produce(topic=topic, value=b"Kafka on the Shore")
producer.flush()