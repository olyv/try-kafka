package com.olyv.event.producer;

import com.olyv.entity.FlightStatus;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FlightEventProducerTest {

    private static final String TOPIC = "topic";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private EventsFactory eventsFactory;

    @InjectMocks
    private FlightEventProducer eventProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(eventProducer, "topic", TOPIC);
    }

    @Test
    public void shouldSendEvent() {
        //Given
        given(eventsFactory.generateEvent())
                .willReturn(new FlightStatus(KEY, VALUE));

        CompletableFuture<SendResult<String, String>> completableFuture = new CompletableFuture<>();
        completableFuture.complete(new SendResult<>(new ProducerRecord<>(TOPIC, KEY, VALUE), null));
        given(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .willReturn(completableFuture);

        //When
        eventProducer.generateEvents();

        //Then
        verify(kafkaTemplate).send(TOPIC, KEY, VALUE);
    }
}