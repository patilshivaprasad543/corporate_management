package com.corporate.travel;

import com.corporate.travel.dto.AiDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "employee", roles = {"EMPLOYEE"})
class AIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAITravelAssistantQuery() throws Exception {
        AiDto.ChatQueryRequest request = AiDto.ChatQueryRequest.builder()
                .message("Find me flights to Singapore under 50000")
                .build();

        mockMvc.perform(post("/api/ai/travel-assistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.response").isNotEmpty())
                .andExpect(jsonPath("$.data.intent").value("FLIGHT_SEARCH"));
    }

    @Test
    void testAITripOptimizer() throws Exception {
        mockMvc.perform(get("/api/ai/trip-optimizer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.savingsAmount").isNumber())
                .andExpect(jsonPath("$.data.withinPolicy").value(true));
    }
}
