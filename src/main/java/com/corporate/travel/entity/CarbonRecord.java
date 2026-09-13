package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "carbon_records")

public class CarbonRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "activity_type", length = 50)
    private String activityType;

    @Column(name = "co2_emissions_kg", precision = 10, scale = 2)
    private BigDecimal co2EmissionsKg;

    @Column(name = "distance_km", precision = 10, scale = 2)
    private BigDecimal distanceKm;


    public CarbonRecord() {}

    public CarbonRecord(Booking booking, User user, Department department, String activityType, BigDecimal co2EmissionsKg, BigDecimal distanceKm) {
        this.booking = booking;
        this.user = user;
        this.department = department;
        this.activityType = activityType;
        this.co2EmissionsKg = co2EmissionsKg;
        this.distanceKm = distanceKm;
    }

    public Booking getBooking() { return booking; }

    public void setBooking(Booking booking) { this.booking = booking; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public String getActivityType() { return activityType; }

    public void setActivityType(String activityType) { this.activityType = activityType; }

    public BigDecimal getCo2EmissionsKg() { return co2EmissionsKg; }

    public void setCo2EmissionsKg(BigDecimal co2EmissionsKg) { this.co2EmissionsKg = co2EmissionsKg; }

    public BigDecimal getDistanceKm() { return distanceKm; }

    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }

    public static CarbonRecordBuilder builder() { return new CarbonRecordBuilder(); }

    public static class CarbonRecordBuilder {
        private Long id;
        private Booking booking;
        private User user;
        private Department department;
        private String activityType;
        private BigDecimal co2EmissionsKg;
        private BigDecimal distanceKm;

        public CarbonRecordBuilder id(Long id) { this.id = id; return this; }
        public CarbonRecordBuilder booking(Booking booking) { this.booking = booking; return this; }
        public CarbonRecordBuilder user(User user) { this.user = user; return this; }
        public CarbonRecordBuilder department(Department department) { this.department = department; return this; }
        public CarbonRecordBuilder activityType(String activityType) { this.activityType = activityType; return this; }
        public CarbonRecordBuilder co2EmissionsKg(BigDecimal co2EmissionsKg) { this.co2EmissionsKg = co2EmissionsKg; return this; }
        public CarbonRecordBuilder distanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; return this; }

        public CarbonRecord build() {
            CarbonRecord obj = new CarbonRecord();
            obj.setId(this.id);
            obj.setBooking(this.booking);
            obj.setUser(this.user);
            obj.setDepartment(this.department);
            obj.setActivityType(this.activityType);
            obj.setCo2EmissionsKg(this.co2EmissionsKg);
            obj.setDistanceKm(this.distanceKm);
            return obj;
        }
    }
}
