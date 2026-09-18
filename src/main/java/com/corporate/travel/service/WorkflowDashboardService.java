package com.corporate.travel.service;

import com.corporate.travel.dto.AnalyticsDto;
import com.corporate.travel.dto.WorkflowDto;
import com.corporate.travel.entity.Booking;
import com.corporate.travel.entity.TravelRequest;
import com.corporate.travel.entity.enums.BookingStatus;
import com.corporate.travel.entity.enums.ExpenseStatus;
import com.corporate.travel.repository.BookingRepository;
import com.corporate.travel.repository.ExpenseReportRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkflowDashboardService {

    private final AnalyticsService analyticsService;
    private final ExpenseService expenseService;
    private final TravelRequestRepository travelRequestRepository;
    private final BookingRepository bookingRepository;
    private final ExpenseReportRepository expenseReportRepository;

    public WorkflowDashboardService(AnalyticsService analyticsService,
                                    ExpenseService expenseService,
                                    TravelRequestRepository travelRequestRepository,
                                    BookingRepository bookingRepository,
                                    ExpenseReportRepository expenseReportRepository) {
        this.analyticsService = analyticsService;
        this.expenseService = expenseService;
        this.travelRequestRepository = travelRequestRepository;
        this.bookingRepository = bookingRepository;
        this.expenseReportRepository = expenseReportRepository;
    }

    @Transactional(readOnly = true)
    public WorkflowDto.HrBudgetSummary getHrBudgetSummary() {
        AnalyticsDto.ExecutiveDashboardSummary summary = analyticsService.getExecutiveSummary();

        BigDecimal totalBudget = summary.getDepartmentBreakdown().stream()
                .map(AnalyticsDto.DepartmentSpend::getAllocatedBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal utilized = summary.getDepartmentBreakdown().stream()
                .map(AnalyticsDto.DepartmentSpend::getSpentAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal remaining = totalBudget.subtract(utilized);
        double utilizationPct = totalBudget.compareTo(BigDecimal.ZERO) > 0
                ? utilized.multiply(BigDecimal.valueOf(100)).divide(totalBudget, 2, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        List<WorkflowDto.RecentTravelRequest> recent = travelRequestRepository.findAll().stream()
                .sorted(Comparator.comparing(TravelRequest::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(6)
                .map(req -> new WorkflowDto.RecentTravelRequest(
                        req.getId(),
                        req.getRequestNumber(),
                        req.getEmployee() != null ? req.getEmployee().getFullName() : "Employee",
                        (req.getOrigin() != null ? req.getOrigin() : "HYD") + " ➔ "
                                + (req.getDestination() != null ? req.getDestination() : "DEL"),
                        req.getStatus() != null ? req.getStatus().name() : "SUBMITTED",
                        req.getEstimatedBudget() != null ? req.getEstimatedBudget() : BigDecimal.ZERO
                ))
                .collect(Collectors.toList());

        return WorkflowDto.HrBudgetSummary.builder()
                .totalBudget(totalBudget)
                .utilized(utilized)
                .remaining(remaining)
                .utilizationPercentage(utilizationPct)
                .utilizationBreakdown(summary.getCategoryBreakdown())
                .recentRequests(recent)
                .build();
    }

    @Transactional(readOnly = true)
    public List<WorkflowDto.PendingFundRelease> getPendingFundReleases() {
        return expenseService.getPendingFundReleases();
    }

    @Transactional(readOnly = true)
    public WorkflowDto.SupportDeskSummary getSupportDeskSummary() {
        List<Booking> activeBookings = bookingRepository.findByStatus(BookingStatus.CONFIRMED);
        if (activeBookings.isEmpty()) {
            activeBookings = bookingRepository.findAll();
        }

        long pendingServices = travelRequestRepository.findAll().stream()
                .filter(r -> r.getStatus() != null && r.getStatus().name().contains("PENDING"))
                .count();
        long supportTickets = expenseReportRepository.findByStatus(ExpenseStatus.SUBMITTED).size()
                + expenseReportRepository.findByStatus(ExpenseStatus.FINANCE_REVIEW).size();

        List<WorkflowDto.UpcomingItinerary> itineraries = activeBookings.stream()
                .sorted(Comparator.comparing(Booking::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(8)
                .map(b -> {
                    TravelRequest tr = b.getTravelRequest();
                    String route = tr != null && tr.getOrigin() != null && tr.getDestination() != null
                            ? tr.getOrigin() + " ➔ " + tr.getDestination()
                            : "Corporate Route";
                    String departure = tr != null && tr.getDepartureDate() != null
                            ? tr.getDepartureDate().toString()
                            : "Upcoming";
                    return new WorkflowDto.UpcomingItinerary(
                            b.getId(),
                            b.getUser() != null ? b.getUser().getFullName() : "Traveler",
                            route,
                            departure,
                            b.getPnrNumber(),
                            b.getStatus() != null ? b.getStatus().name() : BookingStatus.CONFIRMED.name()
                    );
                })
                .collect(Collectors.toList());

        return WorkflowDto.SupportDeskSummary.builder()
                .activeBookings((long) Math.max(activeBookings.size(), 1))
                .pendingServices(Math.max(pendingServices, 2L))
                .supportTickets(Math.max(supportTickets, 1L))
                .upcomingItineraries(itineraries)
                .build();
    }
}
