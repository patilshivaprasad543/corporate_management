package com.corporate.travel;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkflowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "hr", roles = {"HR"})
    void testHrBudgetSummary() throws Exception {
        mockMvc.perform(get("/api/analytics/hr-budget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalBudget").isNumber())
                .andExpect(jsonPath("$.data.utilizationBreakdown").isArray());
    }

    @Test
    @WithMockUser(username = "finance", roles = {"FINANCE"})
    void testPendingFundReleases() throws Exception {
        mockMvc.perform(get("/api/finance/pending-releases"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "support", roles = {"SUPPORT"})
    void testSupportDesk() throws Exception {
        mockMvc.perform(get("/api/support/desk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.activeBookings").isNumber())
                .andExpect(jsonPath("$.data.upcomingItineraries").isArray());
    }
}
