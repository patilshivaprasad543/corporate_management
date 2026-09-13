package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "vendors")

public class Vendor extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "vendor_type", length = 50)
    private String vendorType;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "contract_number", length = 100)
    private String contractNumber;

    @Column(name = "negotiated_discount_percent")
    private Double negotiatedDiscountPercent;

    @Column(name = "sla_rating")
    private Double slaRating;

    @Column(name = "is_preferred")

    private Boolean preferred = true;

    @Column(name = "is_active")

    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;


    public Vendor() {}

    public Vendor(String name, String vendorType, String contactEmail, String contactPhone, String contractNumber, Double negotiatedDiscountPercent, Double slaRating, Boolean preferred, Boolean active, Organization organization) {
        this.name = name;
        this.vendorType = vendorType;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contractNumber = contractNumber;
        this.negotiatedDiscountPercent = negotiatedDiscountPercent;
        this.slaRating = slaRating;
        this.preferred = preferred;
        this.active = active;
        this.organization = organization;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getVendorType() { return vendorType; }

    public void setVendorType(String vendorType) { this.vendorType = vendorType; }

    public String getContactEmail() { return contactEmail; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }

    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getContractNumber() { return contractNumber; }

    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public Double getNegotiatedDiscountPercent() { return negotiatedDiscountPercent; }

    public void setNegotiatedDiscountPercent(Double negotiatedDiscountPercent) { this.negotiatedDiscountPercent = negotiatedDiscountPercent; }

    public Double getSlaRating() { return slaRating; }

    public void setSlaRating(Double slaRating) { this.slaRating = slaRating; }

    public Boolean isPreferred() { return preferred; }

    public Boolean getPreferred() { return preferred; }

    public void setPreferred(Boolean preferred) { this.preferred = preferred; }

    public Boolean isActive() { return active; }

    public Boolean getActive() { return active; }

    public void setActive(Boolean active) { this.active = active; }

    public Organization getOrganization() { return organization; }

    public void setOrganization(Organization organization) { this.organization = organization; }

    public static VendorBuilder builder() { return new VendorBuilder(); }

    public static class VendorBuilder {
        private Long id;
        private String name;
        private String vendorType;
        private String contactEmail;
        private String contactPhone;
        private String contractNumber;
        private Double negotiatedDiscountPercent;
        private Double slaRating;
        private Boolean preferred = true;
        private Boolean active = true;
        private Organization organization;

        public VendorBuilder id(Long id) { this.id = id; return this; }
        public VendorBuilder name(String name) { this.name = name; return this; }
        public VendorBuilder vendorType(String vendorType) { this.vendorType = vendorType; return this; }
        public VendorBuilder contactEmail(String contactEmail) { this.contactEmail = contactEmail; return this; }
        public VendorBuilder contactPhone(String contactPhone) { this.contactPhone = contactPhone; return this; }
        public VendorBuilder contractNumber(String contractNumber) { this.contractNumber = contractNumber; return this; }
        public VendorBuilder negotiatedDiscountPercent(Double negotiatedDiscountPercent) { this.negotiatedDiscountPercent = negotiatedDiscountPercent; return this; }
        public VendorBuilder slaRating(Double slaRating) { this.slaRating = slaRating; return this; }
        public VendorBuilder preferred(Boolean preferred) { this.preferred = preferred; return this; }
        public VendorBuilder active(Boolean active) { this.active = active; return this; }
        public VendorBuilder organization(Organization organization) { this.organization = organization; return this; }

        public Vendor build() {
            Vendor obj = new Vendor();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setVendorType(this.vendorType);
            obj.setContactEmail(this.contactEmail);
            obj.setContactPhone(this.contactPhone);
            obj.setContractNumber(this.contractNumber);
            obj.setNegotiatedDiscountPercent(this.negotiatedDiscountPercent);
            obj.setSlaRating(this.slaRating);
            obj.setPreferred(this.preferred);
            obj.setActive(this.active);
            obj.setOrganization(this.organization);
            return obj;
        }
    }
}
