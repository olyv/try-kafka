package com.olyv.event.consumer;

import com.olyv.event.FlightStatusEvent;
import com.olyv.service.FlightStatusService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class FlightEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(FlightEventConsumer.class);

    @Autowired
    private FlightStatusService flightStatusService;

    @KafkaListener(topics = {"${spring.kafka.topic}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(ConsumerRecord<String, FlightStatusEvent> record) {
        LOG.debug("received event {} {}", record.key(), record.value());
        flightStatusService.saveFlightStatus(record.key(), record.value());
    }
}
