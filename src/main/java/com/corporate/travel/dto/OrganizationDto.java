package com.corporate.travel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrganizationDto {

    public static class CompanyOption {
        private Long id;
        private String name;
        private String code;
        private String country;
        private String currencyCode;

        public CompanyOption() {}

        public CompanyOption(Long id, String name, String code, String country, String currencyCode) {
            this.id = id;
            this.name = name;
            this.code = code;
            this.country = country;
            this.currencyCode = currencyCode;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    }

    public static class DepartmentOption {
        private Long id;
        private String name;
        private String code;

        public DepartmentOption() {}

        public DepartmentOption(Long id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }

    public static class CompanyResponse {
        private Long id;
        private String name;
        private String code;
        private String registrationNumber;
        private String taxNumber;
        private String email;
        private String phone;
        private String domainName;
        private String addressLine1;
        private String city;
        private String state;
        private String country;
        private String currencyCode;
        private String timezone;
        private BigDecimal annualTravelBudget;
        private Boolean active;
        private long employeeCount;
        private long departmentCount;
        private LocalDateTime createdAt;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getRegistrationNumber() { return registrationNumber; }
        public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
        public String getTaxNumber() { return taxNumber; }
        public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
        public BigDecimal getAnnualTravelBudget() { return annualTravelBudget; }
        public void setAnnualTravelBudget(BigDecimal annualTravelBudget) { this.annualTravelBudget = annualTravelBudget; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
        public long getEmployeeCount() { return employeeCount; }
        public void setEmployeeCount(long employeeCount) { this.employeeCount = employeeCount; }
        public long getDepartmentCount() { return departmentCount; }
        public void setDepartmentCount(long departmentCount) { this.departmentCount = departmentCount; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    }

    public static class CreateCompanyRequest {
        @NotBlank private String name;
        @NotBlank private String code;
        private String registrationNumber;
        private String taxNumber;
        @Email private String email;
        private String phone;
        private String domainName;
        private String addressLine1;
        private String city;
        private String state;
        @NotBlank private String country;
        @NotBlank private String currencyCode;
        private String timezone;
        private BigDecimal annualTravelBudget;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getRegistrationNumber() { return registrationNumber; }
        public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
        public String getTaxNumber() { return taxNumber; }
        public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
        public BigDecimal getAnnualTravelBudget() { return annualTravelBudget; }
        public void setAnnualTravelBudget(BigDecimal annualTravelBudget) { this.annualTravelBudget = annualTravelBudget; }
    }

    public static class UpdateCompanyRequest {
        @NotBlank private String name;
        private String registrationNumber;
        private String taxNumber;
        @Email private String email;
        private String phone;
        private String domainName;
        private String addressLine1;
        private String city;
        private String state;
        @NotBlank private String country;
        @NotBlank private String currencyCode;
        private String timezone;
        private BigDecimal annualTravelBudget;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getRegistrationNumber() { return registrationNumber; }
        public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
        public String getTaxNumber() { return taxNumber; }
        public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getDomainName() { return domainName; }
        public void setDomainName(String domainName) { this.domainName = domainName; }
        public String getAddressLine1() { return addressLine1; }
        public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public String getCurrencyCode() { return currencyCode; }
        public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
        public String getTimezone() { return timezone; }
        public void setTimezone(String timezone) { this.timezone = timezone; }
        public BigDecimal getAnnualTravelBudget() { return annualTravelBudget; }
        public void setAnnualTravelBudget(BigDecimal annualTravelBudget) { this.annualTravelBudget = annualTravelBudget; }
    }

    public static class SystemStats {
        private long totalCompanies;
        private long activeCompanies;
        private long totalUsers;
        private long totalDepartments;
        private long totalTravelRequests;
        private long totalBookings;

        public long getTotalCompanies() { return totalCompanies; }
        public void setTotalCompanies(long totalCompanies) { this.totalCompanies = totalCompanies; }
        public long getActiveCompanies() { return activeCompanies; }
        public void setActiveCompanies(long activeCompanies) { this.activeCompanies = activeCompanies; }
        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
        public long getTotalDepartments() { return totalDepartments; }
        public void setTotalDepartments(long totalDepartments) { this.totalDepartments = totalDepartments; }
        public long getTotalTravelRequests() { return totalTravelRequests; }
        public void setTotalTravelRequests(long totalTravelRequests) { this.totalTravelRequests = totalTravelRequests; }
        public long getTotalBookings() { return totalBookings; }
        public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }
    }
}
