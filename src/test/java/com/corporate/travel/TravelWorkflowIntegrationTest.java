package com.corporate.travel;

import com.corporate.travel.dto.AuthDto;
import com.corporate.travel.dto.TravelRequestDto;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.enums.TripType;
import com.corporate.travel.repository.OrganizationRepository;
import com.corporate.travel.service.AuthService;
import com.corporate.travel.service.TravelRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TravelWorkflowIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private TravelRequestService travelRequestService;

    @Autowired
    private OrganizationRepository organizationRepository;

    private Long acmeOrgId;

    @BeforeEach
    void setUp() {
        acmeOrgId = organizationRepository.findByCode("ACME-GLOBAL")
                .map(Organization::getId)
                .orElseThrow();
    }

    @Test
    void testEmployeeLoginAndTravelRequestCreation() {
        // 1. Authenticate demo employee
        AuthDto.AuthResponse authResponse = authService.login(AuthDto.LoginRequest.builder()
                .usernameOrEmail("employee")
                .password("password123")
                .portal("EMPLOYEE")
                .organizationId(acmeOrgId)
                .build());

        assertNotNull(authResponse);
        assertNotNull(authResponse.getAccessToken());
        assertEquals("employee", authResponse.getUsername());

        // 2. Submit travel request
        TravelRequestDto.Response requestResponse = travelRequestService.createRequest(
                authResponse.getUserId(),
                TravelRequestDto.CreateRequest.builder()
                        .tripName("Client Architecture Summit")
                        .tripType(TripType.CLIENT_VISIT)
                        .origin("Hyderabad (HYD)")
                        .destination("Mumbai (BOM)")
                        .departureDate(LocalDate.now().plusDays(10))
                        .returnDate(LocalDate.now().plusDays(13))
                        .estimatedBudget(BigDecimal.valueOf(18000))
                        .businessJustification("Enterprise client engagement")
                        .build()
        );

        assertNotNull(requestResponse);
        assertNotNull(requestResponse.getRequestNumber());
        assertEquals("Client Architecture Summit", requestResponse.getTripName());
        assertFalse(requestResponse.getApprovalSteps().isEmpty());
    }
}
