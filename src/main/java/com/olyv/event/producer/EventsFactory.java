package com.olyv.event.producer;

import com.olyv.entity.FlightStatus;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

@Component
public class EventsFactory {

    private final Faker faker = new Faker();

    public FlightStatus generateEvent() {
        String key = faker.aviation().flight();
        String value = faker.aviation().flightStatus();
        return new FlightStatus(key, value);
    }
}
