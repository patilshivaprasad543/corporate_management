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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Long acmeOrgId;
    private Long betaOrgId;

    @BeforeEach
    void setUp() {
        acmeOrgId = organizationRepository.findByCode("ACME-GLOBAL")
                .map(Organization::getId)
                .orElseThrow();
        betaOrgId = organizationRepository.findByCode("BETA-IND")
                .map(Organization::getId)
                .orElseThrow();
    }

    @Test
    @DisplayName("Unauthenticated travel request API returns 401")
    void unauthenticatedTravelRequestDenied() throws Exception {
        mockMvc.perform(get("/api/travel-requests/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Employee without audit permission gets 403 on audit API")
    @WithMockUser(username = "employee", authorities = {"ROLE_EMPLOYEE", "PERM_TRAVEL_REQUEST_VIEW"})
    void employeeAuditAccessDenied() throws Exception {
        mockMvc.perform(get("/api/audit"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.data").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("Finance user with audit permission can access audit API")
    @WithMockUser(username = "finance", authorities = {"ROLE_FINANCE", "PERM_AUDIT_VIEW"})
    void financeAuditAccessAllowed() throws Exception {
        mockMvc.perform(get("/api/audit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Employee login with wrong company is denied")
    void employeeWrongCompanyLoginDenied() throws Exception {
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .email("traveler@acmetech.com")
                .password("password123")
                .portal("EMPLOYEE")
                .organizationId(betaOrgId)
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.data").value("PORTAL_ACCESS_DENIED"));
    }

    @Test
    @DisplayName("Employee login without company is rejected")
    void employeeLoginWithoutCompanyDenied() throws Exception {
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .email("traveler@acmetech.com")
                .password("password123")
                .portal("EMPLOYEE")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Employee login with correct company succeeds")
    void employeeCorrectCompanyLoginAllowed() throws Exception {
        AuthDto.LoginRequest request = AuthDto.LoginRequest.builder()
                .email("traveler@acmetech.com")
                .password("password123")
                .portal("EMPLOYEE")
                .organizationId(acmeOrgId)
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Registration requires company and employee ID")
    void registrationRequiresCompany() throws Exception {
        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        String suffix = String.valueOf(System.nanoTime());
        request.setUsername("newhire" + suffix);
        request.setEmail("newhire" + suffix + "@acmetech.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        request.setFirstName("New");
        request.setLastName("Hire");
        request.setEmployeeId("EMP-NEW-" + suffix);
        request.setOrganizationId(acmeOrgId);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Registration rejects wrong email domain for company")
    void registrationRejectsWrongEmailDomain() throws Exception {
        AuthDto.RegisterRequest request = new AuthDto.RegisterRequest();
        request.setUsername("wrongdomain");
        request.setEmail("wrong@other.com");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        request.setFirstName("Wrong");
        request.setLastName("Domain");
        request.setEmployeeId("EMP-WRONG-001");
        request.setOrganizationId(acmeOrgId);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Public company list is available for registration")
    void listCompaniesForRegistration() throws Exception {
        mockMvc.perform(get("/api/auth/companies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").exists());
    }
}
