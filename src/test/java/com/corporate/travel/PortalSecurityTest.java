package com.corporate.travel;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.entity.Organization;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PortalSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Long acmeOrgId;

    @BeforeEach
    void setUp() {
        acmeOrgId = organizationRepository.findByCode("ACME-GLOBAL")
                .map(Organization::getId)
                .orElseThrow();
    }

    private void assertLoginAllowed(String email, String portal) throws Exception {
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .email(email)
                .password("password123")
                .portal(portal)
                .organizationId("SUPER_ADMIN".equals(portal) ? null : acmeOrgId)
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    private void assertLoginDenied(String email, String portal) throws Exception {
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .email(email)
                .password("password123")
                .portal(portal)
                .organizationId(acmeOrgId)
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.data").value("PORTAL_ACCESS_DENIED"));
    }

    @Test
    @DisplayName("Employee → Employee portal ALLOWED")
    void employeeEmployeePortalAllowed() throws Exception {
        assertLoginAllowed("traveler@acmetech.com", "EMPLOYEE");
    }

    @Test
    @DisplayName("Employee → Manager portal DENIED")
    void employeeManagerPortalDenied() throws Exception {
        assertLoginDenied("traveler@acmetech.com", "MANAGER");
    }

    @Test
    @DisplayName("Employee → Finance portal DENIED")
    void employeeFinancePortalDenied() throws Exception {
        assertLoginDenied("traveler@acmetech.com", "FINANCE");
    }

    @Test
    @DisplayName("Employee → Super Admin portal DENIED")
    void employeeSuperAdminPortalDenied() throws Exception {
        assertLoginDenied("traveler@acmetech.com", "SUPER_ADMIN");
    }

    @Test
    @DisplayName("Vendor → Employee portal DENIED")
    void vendorEmployeePortalDenied() throws Exception {
        assertLoginDenied("partner@indigoair.com", "EMPLOYEE");
    }

    @Test
    @DisplayName("Vendor → Super Admin portal DENIED")
    void vendorSuperAdminPortalDenied() throws Exception {
        assertLoginDenied("partner@indigoair.com", "SUPER_ADMIN");
    }

    @Test
    @DisplayName("Vendor → Vendor portal ALLOWED")
    void vendorVendorPortalAllowed() throws Exception {
        assertLoginAllowed("partner@indigoair.com", "VENDOR");
    }

    @Test
    @DisplayName("Finance → Super Admin portal DENIED")
    void financeSuperAdminPortalDenied() throws Exception {
        assertLoginDenied("finance@acmetech.com", "SUPER_ADMIN");
    }

    @Test
    @DisplayName("Finance → Finance portal ALLOWED")
    void financeFinancePortalAllowed() throws Exception {
        assertLoginAllowed("finance@acmetech.com", "FINANCE");
    }

    @Test
    @DisplayName("Travel Agent → Finance portal DENIED")
    void travelAgentFinancePortalDenied() throws Exception {
        assertLoginDenied("support@corporatetravel.com", "FINANCE");
    }

    @Test
    @DisplayName("Travel Agent → Travel Agent portal ALLOWED")
    void travelAgentPortalAllowed() throws Exception {
        assertLoginAllowed("support@corporatetravel.com", "TRAVEL_AGENT");
    }

    @Test
    @DisplayName("Super Admin → Super Admin portal ALLOWED")
    void superAdminPortalAllowed() throws Exception {
        assertLoginAllowed("superadmin@corporatetravel.com", "SUPER_ADMIN");
    }

    @Test
    @DisplayName("Manager → Manager portal ALLOWED")
    void managerPortalAllowed() throws Exception {
        assertLoginAllowed("manager@acmetech.com", "MANAGER");
    }

    @Test
    @DisplayName("Travel Admin → Travel Admin portal ALLOWED")
    void travelAdminPortalAllowed() throws Exception {
        assertLoginAllowed("travelmgr@acmetech.com", "TRAVEL_ADMIN");
    }
}
