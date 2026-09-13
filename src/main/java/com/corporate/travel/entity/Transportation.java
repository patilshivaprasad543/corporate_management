package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transportation")

public class Transportation extends BaseEntity {

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @Column(name = "transport_type", length = 50)
    private String transportType;

    @Column(name = "pickup_location", length = 200)
    private String pickupLocation;

    @Column(name = "drop_location", length = 200)
    private String dropLocation;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "vehicle_model", length = 100)
    private String vehicleModel;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";


    public Transportation() {}

    public Transportation(String providerName, String transportType, String pickupLocation, String dropLocation, LocalDateTime pickupTime, String vehicleModel, BigDecimal price, String currencyCode) {
        this.providerName = providerName;
        this.transportType = transportType;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.pickupTime = pickupTime;
        this.vehicleModel = vehicleModel;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    public String getProviderName() { return providerName; }

    public void setProviderName(String providerName) { this.providerName = providerName; }

    public String getTransportType() { return transportType; }

    public void setTransportType(String transportType) { this.transportType = transportType; }

    public String getPickupLocation() { return pickupLocation; }

    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropLocation() { return dropLocation; }

    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public LocalDateTime getPickupTime() { return pickupTime; }

    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

    public String getVehicleModel() { return vehicleModel; }

    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public static TransportationBuilder builder() { return new TransportationBuilder(); }

    public static class TransportationBuilder {
        private Long id;
        private String providerName;
        private String transportType;
        private String pickupLocation;
        private String dropLocation;
        private LocalDateTime pickupTime;
        private String vehicleModel;
        private BigDecimal price;
        private String currencyCode = "INR";

        public TransportationBuilder id(Long id) { this.id = id; return this; }
        public TransportationBuilder providerName(String providerName) { this.providerName = providerName; return this; }
        public TransportationBuilder transportType(String transportType) { this.transportType = transportType; return this; }
        public TransportationBuilder pickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; return this; }
        public TransportationBuilder dropLocation(String dropLocation) { this.dropLocation = dropLocation; return this; }
        public TransportationBuilder pickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; return this; }
        public TransportationBuilder vehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; return this; }
        public TransportationBuilder price(BigDecimal price) { this.price = price; return this; }
        public TransportationBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }

        public Transportation build() {
            Transportation obj = new Transportation();
            obj.setId(this.id);
            obj.setProviderName(this.providerName);
            obj.setTransportType(this.transportType);
            obj.setPickupLocation(this.pickupLocation);
            obj.setDropLocation(this.dropLocation);
            obj.setPickupTime(this.pickupTime);
            obj.setVehicleModel(this.vehicleModel);
            obj.setPrice(this.price);
            obj.setCurrencyCode(this.currencyCode);
            return obj;
        }
    }
}
