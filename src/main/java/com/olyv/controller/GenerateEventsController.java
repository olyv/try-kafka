package com.olyv.controller;

import com.olyv.event.producer.FlightEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GenerateEventsController {

    @Autowired
    private FlightEventProducer eventProducer;

    @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> generateEvents() {
        try {
            eventProducer.generateEvents();
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(Map.of("response", "Flight event is not generated " + e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("response", "Sent request to generate flight event"));
    }
}
