package com.olyv.event.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FlightEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(FlightEventConsumer.class);

    @KafkaListener(topics = {"${spring.kafka.topic}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, String> record) {
        LOG.info("received event {} {}", record.key(), record.value());
    }
}
