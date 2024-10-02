package com.olyv;

import com.olyv.event.FlightStatusEvent;
import com.olyv.persistance.FlightStatus;
import com.olyv.persistance.FlightStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Optional;

import static com.olyv.data.TestData.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = TryKafkaApplication.class)
@TestPropertySource(
        properties = {
                "spring.kafka.topic=" + KAFKA_TOPIC,
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.kafka.consumer.group-id=flight-events-listener-group"
        }
)
@Testcontainers
public class ProduceEventIntegrationTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
            DockerImageName.parse("mongo:4.0.10")
    ).withExposedPorts(27017);

    @Autowired
    private KafkaTemplate<String, FlightStatusEvent> kafkaTemplate;

    @Autowired
    private FlightStatusRepository repository;

    @DynamicPropertySource
    static void containersProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.kafka.producer.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.kafka.consumer.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    public void shouldSaveEvent() {
        //Given
        var event = new FlightStatusEvent(FLIGHT_NUMBER, FLIGHT_STATUS);

        //When
        kafkaTemplate.send(KAFKA_TOPIC, REQUEST_ID, event);

        //Then
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(15, SECONDS)
                .untilAsserted(() -> {
                    Optional<FlightStatus> flightStatus = repository.findByCreatedBy(REQUEST_ID);
                    assertThat(flightStatus)
                            .isPresent()
                            .get()
                            .hasFieldOrPropertyWithValue("flight", FLIGHT_NUMBER)
                            .hasFieldOrPropertyWithValue("status", FLIGHT_STATUS)
                            .hasFieldOrPropertyWithValue("createdBy", REQUEST_ID);
                });
    }
}
