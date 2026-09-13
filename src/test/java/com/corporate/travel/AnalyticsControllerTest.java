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
@WithMockUser(username = "finance", authorities = {"ROLE_FINANCE", "PERM_ANALYTICS_VIEW"})
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testExecutiveAnalyticsSummary() throws Exception {
        mockMvc.perform(get("/api/analytics/executive-summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalTravelSpend").isNumber())
                .andExpect(jsonPath("$.data.policyComplianceRate").isNumber())
                .andExpect(jsonPath("$.data.monthlyTrends").isArray())
                .andExpect(jsonPath("$.data.departmentBreakdown").isArray());
    }
}
