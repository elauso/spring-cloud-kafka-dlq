from json import dumps
from kafka import KafkaProducer
from kafka.errors import KafkaError

# python3 -m venv .venv
# source .venv/bin/activate
# pip install kafka-python

producer = KafkaProducer(
    bootstrap_servers=['localhost:9092'],
    value_serializer=lambda x: dumps(x).encode('utf-8'))

data = {'id':1,'name':'test','document':'1234567890','email':'test@test.com'}
future = producer.send('queueing.example.customer.updated', value=data)

try:
    record_metadata = future.get(timeout=10)
except KafkaError:
    log.exception()
    pass