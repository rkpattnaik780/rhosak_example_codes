import time
import os
from confluent_kafka import Producer

import requests

from dotenv import load_dotenv

load_dotenv()

def _get_token(config):
    payload = {"grant_type": "client_credentials", "scope": "api.iam.service_accounts"}
    resp = requests.post(
        os.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL"),
        auth=(os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID"), os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET")),
        data=payload,
    )
    token = resp.json()
    return token["access_token"], time.time() + float(token["expires_in"])

topic = "prices"
producer_conf = {
    "bootstrap.servers": os.getenv("KAFKA_HOST"),
    "security.protocol": "SASL_SSL",
    "sasl.mechanisms": "OAUTHBEARER",
    "oauth_cb": _get_token,
}
producer = Producer(producer_conf)

producer.produce(topic=topic, value=b"Kafka on the Shore")
producer.flush()