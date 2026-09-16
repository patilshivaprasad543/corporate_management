package com.corporate.travel.service;

import com.corporate.travel.dto.BookingDto;
import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.entity.ExpenseReport;
import com.corporate.travel.entity.Organization;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.User;
import com.corporate.travel.entity.enums.BookingType;
import com.corporate.travel.entity.enums.ExpenseStatus;
import com.corporate.travel.entity.enums.PaymentMethod;
import com.corporate.travel.entity.enums.RequestStatus;
import com.corporate.travel.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkflowConcurrencyGuardTest {

    @Mock BookingRepository bookingRepository;
    @Mock TravelRequestRepository travelRequestRepository;
    @Mock UserRepository userRepository;
    @Mock OrganizationRepository organizationRepository;
    @Mock ItineraryRepository itineraryRepository;
    @Mock TravelWalletRepository walletRepository;
    @Mock PaymentTransactionRepository paymentRepository;
    @Mock NotificationService notificationService;
    @Mock AuditService auditService;

    @InjectMocks BookingService bookingService;

    @Mock ExpenseReportRepository expenseReportRepository;
    @Mock TravelRequestRepository expenseTravelRequestRepository;
    @Mock TravelWalletRepository expenseWalletRepository;
    @Mock UserRepository expenseUserRepository;
    @Mock NotificationService expenseNotificationService;
    @Mock AuditService expenseAuditService;
    @Mock com.corporate.travel.ai.AIExpenseFraudService aiExpenseFraudService;

    @Test
    void bookingUsesLockedTravelRequestAndWalletRows() {
        Organization organization = mock(Organization.class);
        User user = User.builder().id(7L).organization(organization).build();
        User employee = User.builder().id(7L).build();
        TravelRequest request = mock(TravelRequest.class);

        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        when(travelRequestRepository.findByIdForUpdate(11L)).thenReturn(Optional.of(request));
        when(request.getId()).thenReturn(11L);
        when(request.getEmployee()).thenReturn(employee);
        when(request.getStatus()).thenReturn(RequestStatus.APPROVED);
        when(request.getDepartureDate()).thenReturn(LocalDate.now().plusDays(5));
        when(request.getReturnDate()).thenReturn(LocalDate.now().plusDays(8));
        when(request.getTripName()).thenReturn("Client Trip");
        when(bookingRepository.findByTravelRequestId(11L)).thenReturn(List.of());
        when(walletRepository.findByUserIdForUpdate(7L)).thenReturn(Optional.empty());
        when(itineraryRepository.findByUserIdOrderByStartDateDesc(7L)).thenReturn(List.of());
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDto.CreateBookingRequest requestDto = BookingDto.CreateBookingRequest.builder()
                .travelRequestId(11L)
                .bookingType(BookingType.FLIGHT)
                .paymentMethod(PaymentMethod.CORPORATE_CARD)
                .personalBooking(false)
                .build();

        bookingService.createBooking(7L, requestDto);

        verify(travelRequestRepository).findByIdForUpdate(11L);
        verify(walletRepository).findByUserIdForUpdate(7L);
        verify(travelRequestRepository).save(request);
    }

    @Test
    void expenseApprovalUsesLockedExpenseReportRow() {
        ExpenseService expenseService = new ExpenseService(
                expenseReportRepository,
                expenseUserRepository,
                expenseTravelRequestRepository,
                expenseWalletRepository,
                expenseNotificationService,
                expenseAuditService,
                aiExpenseFraudService);

        User employee = User.builder().id(21L).firstName("Test").lastName("Employee").build();
        ExpenseReport report = mock(ExpenseReport.class);
        when(expenseReportRepository.findByIdForUpdate(31L)).thenReturn(Optional.of(report));
        when(report.getStatus()).thenReturn(ExpenseStatus.SUBMITTED);
        when(report.getEmployee()).thenReturn(employee);
        when(report.getTotalAmount()).thenReturn(BigDecimal.valueOf(1250));
        when(expenseReportRepository.save(report)).thenReturn(report);
        when(expenseWalletRepository.findByUserIdForUpdate(21L)).thenReturn(Optional.empty());
        when(report.getItems()).thenReturn(List.of());

        expenseService.approveExpenseReport(31L, 99L, true);

        verify(expenseReportRepository).findByIdForUpdate(31L);
        verify(expenseWalletRepository).findByUserIdForUpdate(21L);
        verify(expenseReportRepository).save(report);
        verify(report).setStatus(ExpenseStatus.APPROVED);
    }
}
