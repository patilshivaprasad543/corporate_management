package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.TravelClass;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_profiles")

public class EmployeeProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "employee_code", length = 50, unique = true)
    private String employeeCode;

    @Column(length = 100)
    private String designation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center_id")
    private CostCenter costCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_travel_class")

    private TravelClass allowedTravelClass = TravelClass.ECONOMY;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_hotel_category")

    private HotelCategory allowedHotelCategory = HotelCategory.STANDARD_3_STAR;

    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Column(name = "passport_expiry")
    private LocalDate passportExpiry;

    @Column(name = "frequent_flyer_number", length = 100)
    private String frequentFlyerNumber;

    @Column(name = "preferred_airline", length = 100)
    private String preferredAirline;

    @Column(name = "preferred_hotel_chain", length = 100)
    private String preferredHotelChain;

    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_phone", length = 50)
    private String emergencyContactPhone;


    public EmployeeProfile() {}

    public EmployeeProfile(User user, String employeeCode, String designation, Department department, CostCenter costCenter, User manager, TravelClass allowedTravelClass, HotelCategory allowedHotelCategory, String passportNumber, LocalDate passportExpiry, String frequentFlyerNumber, String preferredAirline, String preferredHotelChain, String emergencyContactName, String emergencyContactPhone) {
        this.user = user;
        this.employeeCode = employeeCode;
        this.designation = designation;
        this.department = department;
        this.costCenter = costCenter;
        this.manager = manager;
        this.allowedTravelClass = allowedTravelClass;
        this.allowedHotelCategory = allowedHotelCategory;
        this.passportNumber = passportNumber;
        this.passportExpiry = passportExpiry;
        this.frequentFlyerNumber = frequentFlyerNumber;
        this.preferredAirline = preferredAirline;
        this.preferredHotelChain = preferredHotelChain;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getEmployeeCode() { return employeeCode; }

    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getDesignation() { return designation; }

    public void setDesignation(String designation) { this.designation = designation; }

    public Department getDepartment() { return department; }

    public void setDepartment(Department department) { this.department = department; }

    public CostCenter getCostCenter() { return costCenter; }

    public void setCostCenter(CostCenter costCenter) { this.costCenter = costCenter; }

    public User getManager() { return manager; }

    public void setManager(User manager) { this.manager = manager; }

    public TravelClass getAllowedTravelClass() { return allowedTravelClass; }

    public void setAllowedTravelClass(TravelClass allowedTravelClass) { this.allowedTravelClass = allowedTravelClass; }

    public HotelCategory getAllowedHotelCategory() { return allowedHotelCategory; }

    public void setAllowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; }

    public String getPassportNumber() { return passportNumber; }

    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public LocalDate getPassportExpiry() { return passportExpiry; }

    public void setPassportExpiry(LocalDate passportExpiry) { this.passportExpiry = passportExpiry; }

    public String getFrequentFlyerNumber() { return frequentFlyerNumber; }

    public void setFrequentFlyerNumber(String frequentFlyerNumber) { this.frequentFlyerNumber = frequentFlyerNumber; }

    public String getPreferredAirline() { return preferredAirline; }

    public void setPreferredAirline(String preferredAirline) { this.preferredAirline = preferredAirline; }

    public String getPreferredHotelChain() { return preferredHotelChain; }

    public void setPreferredHotelChain(String preferredHotelChain) { this.preferredHotelChain = preferredHotelChain; }

    public String getEmergencyContactName() { return emergencyContactName; }

    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }

    public String getEmergencyContactPhone() { return emergencyContactPhone; }

    public void setEmergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; }

    public static EmployeeProfileBuilder builder() { return new EmployeeProfileBuilder(); }

    public static class EmployeeProfileBuilder {
        private Long id;
        private User user;
        private String employeeCode;
        private String designation;
        private Department department;
        private CostCenter costCenter;
        private User manager;
        private TravelClass allowedTravelClass = TravelClass.ECONOMY;
        private HotelCategory allowedHotelCategory = HotelCategory.STANDARD_3_STAR;
        private String passportNumber;
        private LocalDate passportExpiry;
        private String frequentFlyerNumber;
        private String preferredAirline;
        private String preferredHotelChain;
        private String emergencyContactName;
        private String emergencyContactPhone;

        public EmployeeProfileBuilder id(Long id) { this.id = id; return this; }
        public EmployeeProfileBuilder user(User user) { this.user = user; return this; }
        public EmployeeProfileBuilder employeeCode(String employeeCode) { this.employeeCode = employeeCode; return this; }
        public EmployeeProfileBuilder designation(String designation) { this.designation = designation; return this; }
        public EmployeeProfileBuilder department(Department department) { this.department = department; return this; }
        public EmployeeProfileBuilder costCenter(CostCenter costCenter) { this.costCenter = costCenter; return this; }
        public EmployeeProfileBuilder manager(User manager) { this.manager = manager; return this; }
        public EmployeeProfileBuilder allowedTravelClass(TravelClass allowedTravelClass) { this.allowedTravelClass = allowedTravelClass; return this; }
        public EmployeeProfileBuilder allowedHotelCategory(HotelCategory allowedHotelCategory) { this.allowedHotelCategory = allowedHotelCategory; return this; }
        public EmployeeProfileBuilder passportNumber(String passportNumber) { this.passportNumber = passportNumber; return this; }
        public EmployeeProfileBuilder passportExpiry(LocalDate passportExpiry) { this.passportExpiry = passportExpiry; return this; }
        public EmployeeProfileBuilder frequentFlyerNumber(String frequentFlyerNumber) { this.frequentFlyerNumber = frequentFlyerNumber; return this; }
        public EmployeeProfileBuilder preferredAirline(String preferredAirline) { this.preferredAirline = preferredAirline; return this; }
        public EmployeeProfileBuilder preferredHotelChain(String preferredHotelChain) { this.preferredHotelChain = preferredHotelChain; return this; }
        public EmployeeProfileBuilder emergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; return this; }
        public EmployeeProfileBuilder emergencyContactPhone(String emergencyContactPhone) { this.emergencyContactPhone = emergencyContactPhone; return this; }

        public EmployeeProfile build() {
            EmployeeProfile obj = new EmployeeProfile();
            obj.setId(this.id);
            obj.setUser(this.user);
            obj.setEmployeeCode(this.employeeCode);
            obj.setDesignation(this.designation);
            obj.setDepartment(this.department);
            obj.setCostCenter(this.costCenter);
            obj.setManager(this.manager);
            obj.setAllowedTravelClass(this.allowedTravelClass);
            obj.setAllowedHotelCategory(this.allowedHotelCategory);
            obj.setPassportNumber(this.passportNumber);
            obj.setPassportExpiry(this.passportExpiry);
            obj.setFrequentFlyerNumber(this.frequentFlyerNumber);
            obj.setPreferredAirline(this.preferredAirline);
            obj.setPreferredHotelChain(this.preferredHotelChain);
            obj.setEmergencyContactName(this.emergencyContactName);
            obj.setEmergencyContactPhone(this.emergencyContactPhone);
            return obj;
        }
    }
}
