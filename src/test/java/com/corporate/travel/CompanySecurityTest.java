package com.corporate.travel;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.exception.ForbiddenException;
import com.corporate.travel.repository.OrganizationRepository;
import com.corporate.travel.security.TenantAccessService;
import com.corporate.travel.security.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CompanySecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TenantAccessService tenantAccessService;

    private Long acmeOrgId;
    private Long betaOrgId;
    private String superAdminToken;
    private String financeToken;

    @BeforeEach
    void setUp() throws Exception {
        acmeOrgId = organizationRepository.findByCode("ACME-GLOBAL")
                .map(Organization::getId).orElseThrow();
        betaOrgId = organizationRepository.findByCode("BETA-IND")
                .map(Organization::getId).orElseThrow();

        superAdminToken = loginToken("superadmin@corporatetravel.com", "SUPER_ADMIN", null);
        financeToken = loginToken("finance@acmetech.com", "FINANCE", acmeOrgId);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private String loginToken(String email, String portal, Long organizationId) throws Exception {
        AuthDto.LoginRequest login = AuthDto.LoginRequest.builder()
                .email(email)
                .password("password123")
                .portal(portal)
                .organizationId(organizationId)
                .build();
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
    }

    @Test
    @DisplayName("Super admin can list all companies")
    void superAdminListsCompanies() throws Exception {
        mockMvc.perform(get("/api/companies")
                        .header("Authorization", "Bearer " + superAdminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").exists());
    }

    @Test
    @DisplayName("Super admin can fetch system stats")
    void superAdminSystemStats() throws Exception {
        mockMvc.perform(get("/api/companies/stats")
                        .header("Authorization", "Bearer " + superAdminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCompanies").isNumber())
                .andExpect(jsonPath("$.data.totalUsers").isNumber());
    }

    @Test
    @DisplayName("Super admin can create a company")
    void superAdminCreatesCompany() throws Exception {
        var request = new com.corporate.travel.dto.OrganizationDto.CreateCompanyRequest();
        request.setName("Gamma Corp");
        request.setCode("GAMMA-" + System.nanoTime());
        request.setCountry("India");
        request.setCurrencyCode("INR");
        request.setEmail("admin@gammacorp.com");

        mockMvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer " + superAdminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Gamma Corp"));
    }

    @Test
    @DisplayName("Employee without company permission cannot list companies")
    @WithMockUser(authorities = {"ROLE_EMPLOYEE", "PERM_TRAVEL_REQUEST_VIEW"})
    void employeeCannotListCompanies() throws Exception {
        mockMvc.perform(get("/api/companies"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Finance user can access own company travel requests")
    void financeUserAccessesOwnCompanyRequests() throws Exception {
        mockMvc.perform(get("/api/travel-requests")
                        .header("Authorization", "Bearer " + financeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Tenant access denies cross-company resource access")
    void tenantAccessDeniesCrossCompany() {
        UserPrincipal principal = UserPrincipal.builder()
                .id(1L)
                .username("finance")
                .email("finance@acmetech.com")
                .organizationId(acmeOrgId)
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_FINANCE")))
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

        assertThrows(ForbiddenException.class,
                () -> tenantAccessService.assertCanAccessResource(betaOrgId));
    }
}
