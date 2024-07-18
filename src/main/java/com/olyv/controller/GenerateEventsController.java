package com.olyv.controller;

import com.olyv.event.producer.FlightEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenerateEventsController {

    @Autowired
    private FlightEventProducer eventProducer;

    @GetMapping(path = "/generate")
    public ResponseEntity<String> generateEvents() {
        try {
            eventProducer.generateEvents();
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body("Flight events are not generated " + e.getMessage());
        }
        return ResponseEntity.ok("Flight events are generated");
    }
}
