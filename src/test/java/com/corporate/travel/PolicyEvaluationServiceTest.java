package com.corporate.travel;

import com.corporate.travel.entity.TravelPolicy;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;
import com.corporate.travel.repository.EmployeeProfileRepository;
import com.corporate.travel.repository.TravelPolicyRepository;
import com.corporate.travel.service.PolicyEvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PolicyEvaluationServiceTest {

    @Mock
    private TravelPolicyRepository policyRepository;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    @InjectMocks
    private PolicyEvaluationService policyEvaluationService;

    private TravelPolicy samplePolicy;

    @BeforeEach
    void setUp() {
        samplePolicy = TravelPolicy.builder()
                .maxDomesticFlightPrice(BigDecimal.valueOf(15000))
                .maxInternationalFlightPrice(BigDecimal.valueOf(75000))
                .advanceBookingDays(7)
                .allowedFlightClass(TravelClass.ECONOMY)
                .financeApprovalThreshold(BigDecimal.valueOf(25000))
                .adminApprovalThreshold(BigDecimal.valueOf(100000))
                .build();
    }

    @Test
    void testCompliantDomesticTravelRequest() {
        when(policyRepository.findFirstByOrganizationIdAndActiveTrue(anyLong()))
                .thenReturn(Optional.of(samplePolicy));
        when(employeeProfileRepository.findByUserId(anyLong()))
                .thenReturn(Optional.empty());

        PolicyEvaluationService.EvaluationResult result = policyEvaluationService.evaluateTravelRequest(
                1L, 5L, BigDecimal.valueOf(12000), TravelClass.ECONOMY, LocalDate.now().plusDays(10), false
        );

        assertEquals(PolicyComplianceStatus.COMPLIANT, result.status);
        assertFalse(result.requiresFinanceApproval);
        assertFalse(result.requiresAdminApproval);
    }

    @Test
    void testFinanceApprovalThresholdTriggered() {
        when(policyRepository.findFirstByOrganizationIdAndActiveTrue(anyLong()))
                .thenReturn(Optional.of(samplePolicy));
        when(employeeProfileRepository.findByUserId(anyLong()))
                .thenReturn(Optional.empty());

        PolicyEvaluationService.EvaluationResult result = policyEvaluationService.evaluateTravelRequest(
                1L, 5L, BigDecimal.valueOf(35000), TravelClass.ECONOMY, LocalDate.now().plusDays(10), false
        );

        assertTrue(result.requiresFinanceApproval);
        assertFalse(result.requiresAdminApproval);
    }

    @Test
    void testAdvanceBookingWarning() {
        when(policyRepository.findFirstByOrganizationIdAndActiveTrue(anyLong()))
                .thenReturn(Optional.of(samplePolicy));
        when(employeeProfileRepository.findByUserId(anyLong()))
                .thenReturn(Optional.empty());

        PolicyEvaluationService.EvaluationResult result = policyEvaluationService.evaluateTravelRequest(
                1L, 5L, BigDecimal.valueOf(12000), TravelClass.ECONOMY, LocalDate.now().plusDays(3), false
        );

        assertEquals(PolicyComplianceStatus.WARNING, result.status);
        assertTrue(result.reasons.get(0).contains("advance"));
    }
}
