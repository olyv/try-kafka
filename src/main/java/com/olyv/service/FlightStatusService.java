package com.olyv.service;

import com.olyv.event.FlightStatusEvent;
import com.olyv.event.producer.FlightEventProducer;
import com.olyv.persistance.FlightStatus;
import com.olyv.persistance.FlightStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FlightStatusService {

    @Autowired
    private FlightStatusRepository flightStatusRepository;

    @Autowired
    private FlightEventProducer eventProducer;

    public String generateEvent() {
        var requestId = UUID.randomUUID().toString();
        eventProducer.generateEvents(requestId);
        return requestId;
    }

    public void saveFlightStatus(String requestId, FlightStatusEvent event) {
        var flightStatusEntity = new FlightStatus(event.flight(), event.status(), LocalDateTime.now(), requestId);
        flightStatusRepository.insert(flightStatusEntity);
    }

    public Optional<FlightStatusEvent> getByRequestId(String requestId) {
        return flightStatusRepository.findByCreatedBy(requestId)
                .map(it -> new FlightStatusEvent(it.getFlight(), it.getStatus()));
    }

    public Optional<FlightStatusEvent> getLatestEvent() {
        Optional<FlightStatus> eventEntity = flightStatusRepository.findFirstByOrderByCreatedDesc();
        return eventEntity.map(it -> new FlightStatusEvent(it.getFlight(), it.getStatus()));
    }
}
