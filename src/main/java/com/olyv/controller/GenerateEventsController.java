package com.olyv.controller;

import com.olyv.service.FlightStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class GenerateEventsController {

    @Autowired
    private FlightStatusService flightStatusService;

    @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> generateEvents() {
        var requestId = UUID.randomUUID().toString();
        try {
            requestId = flightStatusService.generateEvent();
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(Map.of("response", "Flight event is not generated %s".formatted(e.getMessage())));
        }
        return ResponseEntity.ok(
                Map.of("response", "Request to generate flight event is filed with id %s".formatted(requestId))
        );
    }
}
