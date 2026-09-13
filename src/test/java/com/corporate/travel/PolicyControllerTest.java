package com.corporate.travel;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.dto.PolicyDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.repository.OrganizationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Long acmeOrgId;
    private String travelAdminToken;

    @BeforeEach
    void setUp() throws Exception {
        acmeOrgId = organizationRepository.findByCode("ACME-GLOBAL")
                .map(Organization::getId).orElseThrow();

        AuthDto.LoginRequest login = AuthDto.LoginRequest.builder()
                .email("travelmgr@acmetech.com")
                .password("password123")
                .portal("TRAVEL_ADMIN")
                .organizationId(acmeOrgId)
                .build();
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();
        travelAdminToken = objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
    }

    @Test
    @DisplayName("Travel admin can list policies")
    void listPolicies() throws Exception {
        mockMvc.perform(get("/api/policies")
                        .header("Authorization", "Bearer " + travelAdminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("Policy evaluation detects flight budget violation with difference")
    void evaluatePolicyViolation() throws Exception {
        PolicyDto.EvaluateRequest request = new PolicyDto.EvaluateRequest();
        request.setOrganizationId(acmeOrgId);
        request.setFlightAmount(BigDecimal.valueOf(48000));
        request.setEstimatedBudget(BigDecimal.valueOf(48000));
        request.setTravelClass(TravelClass.ECONOMY);
        request.setDepartureDate(LocalDate.now().plusDays(14));
        request.setInternational(false);

        mockMvc.perform(post("/api/policies/evaluate")
                        .header("Authorization", "Bearer " + travelAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("POLICY_VIOLATION"))
                .andExpect(jsonPath("$.data.violations[0].differenceAmount").isNumber())
                .andExpect(jsonPath("$.data.requiresFinanceApproval").value(true));
    }
}
