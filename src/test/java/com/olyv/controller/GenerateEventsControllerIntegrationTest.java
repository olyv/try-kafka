package com.olyv.controller;

import com.olyv.entity.FlightStatus;
import com.olyv.event.producer.EventsFactory;
import com.olyv.event.producer.FlightEventProducer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.olyv.controller.GenerateEventsControllerIntegrationTest.TEST_TOPIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {TEST_TOPIC})
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class GenerateEventsControllerIntegrationTest {

    public static final String TEST_TOPIC = "flights-test-topic";
    private static final int DEFAULT_TIMEOUT = 60;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private FlightEventProducer eventProducer;

    @MockBean
    private EventsFactory eventsFactory;

    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> configs = new HashMap<>(
                KafkaTestUtils.consumerProps("flight-events-listener-group", "true", embeddedKafkaBroker)
        );
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
        ReflectionTestUtils.setField(eventProducer, "topic", TEST_TOPIC);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    public void shouldGenerateEvent_givenRequestSent() {
        //Given
        String eventKey = "TESTFLIGHT";
        String eventValue = "Test status";
        when(eventsFactory.generateEvent())
                .thenReturn(new FlightStatus(eventKey, eventValue));

        //When
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("/generate", Map.class);

        //Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(Map.of("response", "Sent request to generate flight event"));

        ConsumerRecords<String, String> consumerRecords = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(DEFAULT_TIMEOUT));
        assertThat(consumerRecords).hasSize(1);

        ConsumerRecord<String, String> record = consumerRecords.records(TEST_TOPIC).iterator().next();
        assertThat(record.key()).isEqualTo(eventKey);
        assertThat(record.value()).isEqualTo(eventValue);
    }
}