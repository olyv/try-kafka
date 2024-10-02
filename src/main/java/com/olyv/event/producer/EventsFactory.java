package com.olyv.event.producer;

import com.olyv.event.FlightStatusEvent;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

/**
 * Dummy event source
 */
@Component
public class EventsFactory {

    private final Faker faker = new Faker();

    public FlightStatusEvent generateEvent() {
        String key = faker.aviation().flight();
        String value = faker.aviation().flightStatus();
        return new FlightStatusEvent(key, value);
    }
}
