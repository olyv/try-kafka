package com.olyv.controller;

import com.olyv.event.producer.FlightEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenerateEventsController.class)
class GenerateEventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightEventProducer eventProducer;

    @Test
    public void shouldCallEventProducer() throws Exception {
        //Given
        doNothing().when(eventProducer).generateEvents();

        //When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/generate"));

        //Then
        response.andExpect(status().isOk())
                .andExpect(content().json("{\"response\":\"Sent request to generate flight event\"}"));
    }
}