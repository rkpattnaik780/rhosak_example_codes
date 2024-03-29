import time
import os
import argparse
from confluent_kafka import Consumer

import requests

from dotenv import load_dotenv

parser = argparse.ArgumentParser(description='Python Kafka consumer')
parser.add_argument('--max-messages', metavar='N', type=int,
                    help='Maximum number of messages to be consumed', default=1)

args = parser.parse_args()

load_dotenv()

kafka_host = os.getenv("KAFKA_HOST")
rhoas_client_id = os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_ID")
rhoas_client_secret = os.getenv("RHOAS_SERVICE_ACCOUNT_CLIENT_SECRET")
rhoas_oauth_token_url = os.getenv("RHOAS_SERVICE_ACCOUNT_OAUTH_TOKEN_URL")


def _get_token(config):
    payload = {"grant_type": "client_credentials", "scope": "api.iam.service_accounts"}
    resp = requests.post(
        rhoas_oauth_token_url,
        auth=(
            rhoas_client_id,
            rhoas_client_secret,
        ),
        data=payload,
    )
    token = resp.json()
    return token["access_token"], time.time() + float(token["expires_in"])

topic = "prices"

consumer_conf = {
    "bootstrap.servers": kafka_host,
    "security.protocol": "SASL_SSL",
    "sasl.mechanisms": "OAUTHBEARER",
    "group.id": "test-group",
    "session.timeout.ms": 6000,
    "auto.offset.reset": "earliest",
    "oauth_cb": _get_token,
}
consumer = Consumer(consumer_conf)

consumer.subscribe([topic])

messages_count = 0

while True:
    if messages_count >= args.max_messages:
        break
    try:
        # SIGINT can't be handled when polling, limit timeout to 1 second.
        msg = consumer.poll(1.0)
        if msg is None:
            continue

        print(msg.value())
        messages_count = messages_count + 1
    except KeyboardInterrupt:
        break

consumer.close()