package com.olyv.controller;

import com.olyv.service.FlightStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.olyv.data.TestData.REQUEST_ID;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenerateEventsController.class)
class GenerateEventsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightStatusService flightStatusService;

    @Test
    public void shouldCallFlightStatusService() throws Exception {
        //Given
        given(flightStatusService.generateEvent())
                .willReturn(REQUEST_ID);

        //When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/generate"));

        //Then
        response.andExpect(status().isOk())
                .andExpect(content().json("""
                        {"response":"Request to generate flight event is filed with id %s"}
                        """.formatted(REQUEST_ID)));
    }
}