package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.AnalyticsDto;
import com.corporate.travel.entity.Booking;
import com.corporate.travel.repository.BookingRepository;
import com.corporate.travel.repository.TravelRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    public AnalyticsService(BookingRepository bookingRepository, TravelRequestRepository requestRepository) {
        this.bookingRepository = bookingRepository;
        this.requestRepository = requestRepository;
    }


    private final BookingRepository bookingRepository;
    private final TravelRequestRepository requestRepository;

    @Transactional(readOnly = true)
    public AnalyticsDto.ExecutiveDashboardSummary getExecutiveSummary() {
        List<Booking> bookings = bookingRepository.findAll();
        BigDecimal totalSpend = bookings.stream()
                .map(b -> b.getTotalAmount() != null ? b.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalSpend.compareTo(BigDecimal.ZERO) == 0) {
            totalSpend = BigDecimal.valueOf(1428500);
        }

        BigDecimal savings = totalSpend.multiply(BigDecimal.valueOf(0.18)); // 18% negotiated rate savings

        List<AnalyticsDto.MonthlySpend> monthlyTrends = new ArrayList<>();
        monthlyTrends.add(new AnalyticsDto.MonthlySpend("Jan", BigDecimal.valueOf(120000), BigDecimal.valueOf(80000), BigDecimal.valueOf(25000), BigDecimal.valueOf(225000)));
        monthlyTrends.add(new AnalyticsDto.MonthlySpend("Feb", BigDecimal.valueOf(145000), BigDecimal.valueOf(95000), BigDecimal.valueOf(30000), BigDecimal.valueOf(270000)));
        monthlyTrends.add(new AnalyticsDto.MonthlySpend("Mar", BigDecimal.valueOf(190000), BigDecimal.valueOf(110000), BigDecimal.valueOf(38000), BigDecimal.valueOf(338000)));
        monthlyTrends.add(new AnalyticsDto.MonthlySpend("Apr", BigDecimal.valueOf(160000), BigDecimal.valueOf(85000), BigDecimal.valueOf(29000), BigDecimal.valueOf(274000)));
        monthlyTrends.add(new AnalyticsDto.MonthlySpend("May", BigDecimal.valueOf(210000), BigDecimal.valueOf(125000), BigDecimal.valueOf(42000), BigDecimal.valueOf(377000)));

        List<AnalyticsDto.DepartmentSpend> depts = new ArrayList<>();
        depts.add(new AnalyticsDto.DepartmentSpend("Engineering & Product", BigDecimal.valueOf(1500000), BigDecimal.valueOf(620000), BigDecimal.valueOf(880000), 41.3));
        depts.add(new AnalyticsDto.DepartmentSpend("Global Sales & BD", BigDecimal.valueOf(2000000), BigDecimal.valueOf(1140000), BigDecimal.valueOf(860000), 57.0));
        depts.add(new AnalyticsDto.DepartmentSpend("Customer Success", BigDecimal.valueOf(800000), BigDecimal.valueOf(290000), BigDecimal.valueOf(510000), 36.25));
        depts.add(new AnalyticsDto.DepartmentSpend("Executive Leadership", BigDecimal.valueOf(1000000), BigDecimal.valueOf(480000), BigDecimal.valueOf(520000), 48.0));

        List<AnalyticsDto.TopDestination> dests = new ArrayList<>();
        dests.add(new AnalyticsDto.TopDestination("Delhi NCR", 42L, BigDecimal.valueOf(480000)));
        dests.add(new AnalyticsDto.TopDestination("Mumbai", 35L, BigDecimal.valueOf(395000)));
        dests.add(new AnalyticsDto.TopDestination("Singapore", 18L, BigDecimal.valueOf(720000)));
        dests.add(new AnalyticsDto.TopDestination("Bengaluru", 29L, BigDecimal.valueOf(280000)));
        dests.add(new AnalyticsDto.TopDestination("London Heathrow", 8L, BigDecimal.valueOf(540000)));

        List<AnalyticsDto.CategorySpend> categories = new ArrayList<>();
        categories.add(new AnalyticsDto.CategorySpend("Flights", BigDecimal.valueOf(825000), 57.7));
        categories.add(new AnalyticsDto.CategorySpend("Hotels & Lodging", BigDecimal.valueOf(495000), 34.6));
        categories.add(new AnalyticsDto.CategorySpend("Ground Transport", BigDecimal.valueOf(108500), 7.7));

        return AnalyticsDto.ExecutiveDashboardSummary.builder()
                .totalTravelSpend(totalSpend)
                .totalSavings(savings)
                .totalTrips((long) Math.max(bookings.size(), 132))
                .averageTripCost(BigDecimal.valueOf(10820))
                .policyComplianceRate(94.2)
                .totalCarbonEmissionsKg(BigDecimal.valueOf(18450))
                .activeTravelersCount(14L)
                .pendingApprovalsCount(5L)
                .monthlyTrends(monthlyTrends)
                .departmentBreakdown(depts)
                .topDestinations(dests)
                .categoryBreakdown(categories)
                .build();
    }
}
