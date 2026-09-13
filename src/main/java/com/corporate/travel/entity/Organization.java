package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "organizations")

public class Organization extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(name = "domain_name", length = 100)
    private String domainName;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(length = 100)
    private String city;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String country;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Column(name = "annual_travel_budget", precision = 15, scale = 2)

    private BigDecimal annualTravelBudget = BigDecimal.valueOf(5000000);

    @Column(name = "is_active")

    private Boolean active = true;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "tax_number", length = 100)
    private String taxNumber;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(length = 50)
    private String timezone = "UTC";


    public Organization() {}

    public Organization(String name, String code, String domainName, String logoUrl, String addressLine1, String city, String state, String country, String currencyCode, BigDecimal annualTravelBudget, Boolean active) {
        this.name = name;
        this.code = code;
        this.domainName = domainName;
        this.logoUrl = logoUrl;
        this.addressLine1 = addressLine1;
        this.city = city;
        this.state = state;
        this.country = country;
        this.currencyCode = currencyCode;
        this.annualTravelBudget = annualTravelBudget;
        this.active = active;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getDomainName() { return domainName; }

    public void setDomainName(String domainName) { this.domainName = domainName; }

    public String getLogoUrl() { return logoUrl; }

    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

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

    public BigDecimal getAnnualTravelBudget() { return annualTravelBudget; }

    public void setAnnualTravelBudget(BigDecimal annualTravelBudget) { this.annualTravelBudget = annualTravelBudget; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getTaxNumber() { return taxNumber; }
    public void setTaxNumber(String taxNumber) { this.taxNumber = taxNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public static OrganizationBuilder builder() { return new OrganizationBuilder(); }

    public static class OrganizationBuilder {
        private Long id;
        private String name;
        private String code;
        private String domainName;
        private String logoUrl;
        private String addressLine1;
        private String city;
        private String state;
        private String country;
        private String currencyCode = "INR";
        private BigDecimal annualTravelBudget = BigDecimal.valueOf(5000000);
        private Boolean active = true;
        private String registrationNumber;
        private String taxNumber;
        private String email;
        private String phone;
        private String timezone = "UTC";

        public OrganizationBuilder id(Long id) { this.id = id; return this; }
        public OrganizationBuilder name(String name) { this.name = name; return this; }
        public OrganizationBuilder code(String code) { this.code = code; return this; }
        public OrganizationBuilder domainName(String domainName) { this.domainName = domainName; return this; }
        public OrganizationBuilder logoUrl(String logoUrl) { this.logoUrl = logoUrl; return this; }
        public OrganizationBuilder addressLine1(String addressLine1) { this.addressLine1 = addressLine1; return this; }
        public OrganizationBuilder city(String city) { this.city = city; return this; }
        public OrganizationBuilder state(String state) { this.state = state; return this; }
        public OrganizationBuilder country(String country) { this.country = country; return this; }
        public OrganizationBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public OrganizationBuilder annualTravelBudget(BigDecimal annualTravelBudget) { this.annualTravelBudget = annualTravelBudget; return this; }
        public OrganizationBuilder active(Boolean active) { this.active = active; return this; }
        public OrganizationBuilder registrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; return this; }
        public OrganizationBuilder taxNumber(String taxNumber) { this.taxNumber = taxNumber; return this; }
        public OrganizationBuilder email(String email) { this.email = email; return this; }
        public OrganizationBuilder phone(String phone) { this.phone = phone; return this; }
        public OrganizationBuilder timezone(String timezone) { this.timezone = timezone; return this; }

        public Organization build() {
            Organization obj = new Organization();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setCode(this.code);
            obj.setDomainName(this.domainName);
            obj.setLogoUrl(this.logoUrl);
            obj.setAddressLine1(this.addressLine1);
            obj.setCity(this.city);
            obj.setState(this.state);
            obj.setCountry(this.country);
            obj.setCurrencyCode(this.currencyCode);
            obj.setAnnualTravelBudget(this.annualTravelBudget);
            obj.setActive(this.active);
            obj.setRegistrationNumber(this.registrationNumber);
            obj.setTaxNumber(this.taxNumber);
            obj.setEmail(this.email);
            obj.setPhone(this.phone);
            obj.setTimezone(this.timezone);
            return obj;
        }
    }
}
