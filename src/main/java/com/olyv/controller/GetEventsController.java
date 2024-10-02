package com.olyv.controller;

import com.olyv.event.FlightStatusEvent;
import com.olyv.service.FlightStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetEventsController {

    @Autowired
    private FlightStatusService flightStatusService;

    @GetMapping(path = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightStatusEvent> getLatestEvent() {
            return ResponseEntity.of(flightStatusService.getLatestEvent());
    }

    @GetMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightStatusEvent> getEventByRequestId(@PathVariable String requestId) {
        return ResponseEntity.of(flightStatusService.getByRequestId(requestId));
    }
}
