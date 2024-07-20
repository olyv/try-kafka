package com.olyv.event.producer;

import com.olyv.entity.FlightStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class FlightEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(FlightEventProducer.class);

    @Value("${spring.kafka.topic}")
    public String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private EventsFactory eventsFactory;

    public void generateEvents() {
        FlightStatus event = eventsFactory.generateEvent();
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, event.flight(), event.status());
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                LOG.debug("Produced event: {}", result.getProducerRecord());
            }
            else {
                LOG.error("Failed to produce event", ex);
            }
        });
    }
}
