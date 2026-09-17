package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "travel_budgets")
public class TravelBudget extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "travel_request_id", nullable = false, unique = true)
    private TravelRequest travelRequest;

    @Column(name = "allocated_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @Column(name = "flight_limit", precision = 12, scale = 2) private BigDecimal flightLimit = BigDecimal.ZERO;
    @Column(name = "hotel_limit", precision = 12, scale = 2) private BigDecimal hotelLimit = BigDecimal.ZERO;
    @Column(name = "cab_limit", precision = 12, scale = 2) private BigDecimal cabLimit = BigDecimal.ZERO;
    @Column(name = "meal_limit", precision = 12, scale = 2) private BigDecimal mealLimit = BigDecimal.ZERO;
    @Column(name = "other_limit", precision = 12, scale = 2) private BigDecimal otherLimit = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BudgetStatus status = BudgetStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocated_by_user_id")
    private User allocatedBy;

    public enum BudgetStatus { DRAFT, ALLOCATED, HR_APPROVED, PARTIALLY_USED, CLOSED }

    public TravelRequest getTravelRequest() { return travelRequest; }
    public void setTravelRequest(TravelRequest travelRequest) { this.travelRequest = travelRequest; }
    public BigDecimal getAllocatedAmount() { return allocatedAmount; }
    public void setAllocatedAmount(BigDecimal allocatedAmount) { this.allocatedAmount = allocatedAmount; }
    public BigDecimal getFlightLimit() { return flightLimit; }
    public void setFlightLimit(BigDecimal flightLimit) { this.flightLimit = flightLimit; }
    public BigDecimal getHotelLimit() { return hotelLimit; }
    public void setHotelLimit(BigDecimal hotelLimit) { this.hotelLimit = hotelLimit; }
    public BigDecimal getCabLimit() { return cabLimit; }
    public void setCabLimit(BigDecimal cabLimit) { this.cabLimit = cabLimit; }
    public BigDecimal getMealLimit() { return mealLimit; }
    public void setMealLimit(BigDecimal mealLimit) { this.mealLimit = mealLimit; }
    public BigDecimal getOtherLimit() { return otherLimit; }
    public void setOtherLimit(BigDecimal otherLimit) { this.otherLimit = otherLimit; }
    public BudgetStatus getStatus() { return status; }
    public void setStatus(BudgetStatus status) { this.status = status; }
    public User getAllocatedBy() { return allocatedBy; }
    public void setAllocatedBy(User allocatedBy) { this.allocatedBy = allocatedBy; }
}
