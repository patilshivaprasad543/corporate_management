package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.RiskLevel;
import jakarta.persistence.*;

@Entity
@Table(name = "risk_alerts")

public class RiskAlert extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private RiskLevel riskLevel;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "is_active")

    private Boolean active = true;


    public RiskAlert() {}

    public RiskAlert(String destination, String countryCode, RiskLevel riskLevel, String title, String description, String category, Boolean active) {
        this.destination = destination;
        this.countryCode = countryCode;
        this.riskLevel = riskLevel;
        this.title = title;
        this.description = description;
        this.category = category;
        this.active = active;
    }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getCountryCode() { return countryCode; }

    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public RiskLevel getRiskLevel() { return riskLevel; }

    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }

    public void setCategory(String category) { this.category = category; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public static RiskAlertBuilder builder() { return new RiskAlertBuilder(); }

    public static class RiskAlertBuilder {
        private Long id;
        private String destination;
        private String countryCode;
        private RiskLevel riskLevel;
        private String title;
        private String description;
        private String category;
        private Boolean active = true;

        public RiskAlertBuilder id(Long id) { this.id = id; return this; }
        public RiskAlertBuilder destination(String destination) { this.destination = destination; return this; }
        public RiskAlertBuilder countryCode(String countryCode) { this.countryCode = countryCode; return this; }
        public RiskAlertBuilder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public RiskAlertBuilder title(String title) { this.title = title; return this; }
        public RiskAlertBuilder description(String description) { this.description = description; return this; }
        public RiskAlertBuilder category(String category) { this.category = category; return this; }
        public RiskAlertBuilder active(Boolean active) { this.active = active; return this; }

        public RiskAlert build() {
            RiskAlert obj = new RiskAlert();
            obj.setId(this.id);
            obj.setDestination(this.destination);
            obj.setCountryCode(this.countryCode);
            obj.setRiskLevel(this.riskLevel);
            obj.setTitle(this.title);
            obj.setDescription(this.description);
            obj.setCategory(this.category);
            obj.setActive(this.active);
            return obj;
        }
    }
}
