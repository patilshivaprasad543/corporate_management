package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.TravelClass;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")

public class Flight extends BaseEntity {

    @Column(name = "flight_number", nullable = false, length = 50)
    private String flightNumber;

    @Column(nullable = false, length = 100)
    private String airline;

    @Column(name = "airline_code", length = 10)
    private String airlineCode;

    @Column(name = "origin_code", nullable = false, length = 10)
    private String originCode;

    @Column(name = "origin_city", nullable = false, length = 100)
    private String originCity;

    @Column(name = "destination_code", nullable = false, length = 10)
    private String destinationCode;

    @Column(name = "destination_city", nullable = false, length = 100)
    private String destinationCity;

    @Column(name = "departure_time", nullable = false)
    private LocalDateTime departureTime;

    @Column(name = "arrival_time", nullable = false)
    private LocalDateTime arrivalTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "stops_count")

    private Integer stopsCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_class")
    private TravelClass travelClass;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Column(name = "carbon_emissions_kg", precision = 8, scale = 2)
    private BigDecimal carbonEmissionsKg;

    @Column(name = "is_refundable")

    private Boolean refundable = true;

    @Column(name = "baggage_allowance", length = 100)

    private String baggageAllowance = "15 kg Check-in, 7 kg Cabin";

    @Column(name = "is_corporate_preferred")

    private Boolean corporatePreferred = false;


    public Flight() {}

    public Flight(String flightNumber, String airline, String airlineCode, String originCode, String originCity, String destinationCode, String destinationCity, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer durationMinutes, Integer stopsCount, TravelClass travelClass, BigDecimal price, String currencyCode, BigDecimal carbonEmissionsKg, Boolean refundable, String baggageAllowance, Boolean corporatePreferred) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.airlineCode = airlineCode;
        this.originCode = originCode;
        this.originCity = originCity;
        this.destinationCode = destinationCode;
        this.destinationCity = destinationCity;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.durationMinutes = durationMinutes;
        this.stopsCount = stopsCount;
        this.travelClass = travelClass;
        this.price = price;
        this.currencyCode = currencyCode;
        this.carbonEmissionsKg = carbonEmissionsKg;
        this.refundable = refundable;
        this.baggageAllowance = baggageAllowance;
        this.corporatePreferred = corporatePreferred;
    }

    public String getFlightNumber() { return flightNumber; }

    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getAirline() { return airline; }

    public void setAirline(String airline) { this.airline = airline; }

    public String getAirlineCode() { return airlineCode; }

    public void setAirlineCode(String airlineCode) { this.airlineCode = airlineCode; }

    public String getOriginCode() { return originCode; }

    public void setOriginCode(String originCode) { this.originCode = originCode; }

    public String getOriginCity() { return originCity; }

    public void setOriginCity(String originCity) { this.originCity = originCity; }

    public String getDestinationCode() { return destinationCode; }

    public void setDestinationCode(String destinationCode) { this.destinationCode = destinationCode; }

    public String getDestinationCity() { return destinationCity; }

    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public LocalDateTime getDepartureTime() { return departureTime; }

    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public Integer getDurationMinutes() { return durationMinutes; }

    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Integer getStopsCount() { return stopsCount; }

    public void setStopsCount(Integer stopsCount) { this.stopsCount = stopsCount; }

    public TravelClass getTravelClass() { return travelClass; }

    public void setTravelClass(TravelClass travelClass) { this.travelClass = travelClass; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getCarbonEmissionsKg() { return carbonEmissionsKg; }

    public void setCarbonEmissionsKg(BigDecimal carbonEmissionsKg) { this.carbonEmissionsKg = carbonEmissionsKg; }

    public Boolean isRefundable() { return refundable; }

    public Boolean getRefundable() { return refundable; }

    public void setRefundable(Boolean refundable) { this.refundable = refundable; }

    public String getBaggageAllowance() { return baggageAllowance; }

    public void setBaggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; }

    public Boolean isCorporatePreferred() { return corporatePreferred; }

    public Boolean getCorporatePreferred() { return corporatePreferred; }

    public void setCorporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; }

    public static FlightBuilder builder() { return new FlightBuilder(); }

    public static class FlightBuilder {
        private Long id;
        private String flightNumber;
        private String airline;
        private String airlineCode;
        private String originCode;
        private String originCity;
        private String destinationCode;
        private String destinationCity;
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;
        private Integer durationMinutes;
        private Integer stopsCount = 0;
        private TravelClass travelClass;
        private BigDecimal price;
        private String currencyCode = "INR";
        private BigDecimal carbonEmissionsKg;
        private Boolean refundable = true;
        private String baggageAllowance = "15 kg Check-in, 7 kg Cabin";
        private Boolean corporatePreferred = false;

        public FlightBuilder id(Long id) { this.id = id; return this; }
        public FlightBuilder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public FlightBuilder airline(String airline) { this.airline = airline; return this; }
        public FlightBuilder airlineCode(String airlineCode) { this.airlineCode = airlineCode; return this; }
        public FlightBuilder originCode(String originCode) { this.originCode = originCode; return this; }
        public FlightBuilder originCity(String originCity) { this.originCity = originCity; return this; }
        public FlightBuilder destinationCode(String destinationCode) { this.destinationCode = destinationCode; return this; }
        public FlightBuilder destinationCity(String destinationCity) { this.destinationCity = destinationCity; return this; }
        public FlightBuilder departureTime(LocalDateTime departureTime) { this.departureTime = departureTime; return this; }
        public FlightBuilder arrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public FlightBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public FlightBuilder stopsCount(Integer stopsCount) { this.stopsCount = stopsCount; return this; }
        public FlightBuilder travelClass(TravelClass travelClass) { this.travelClass = travelClass; return this; }
        public FlightBuilder price(BigDecimal price) { this.price = price; return this; }
        public FlightBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public FlightBuilder carbonEmissionsKg(BigDecimal carbonEmissionsKg) { this.carbonEmissionsKg = carbonEmissionsKg; return this; }
        public FlightBuilder refundable(Boolean refundable) { this.refundable = refundable; return this; }
        public FlightBuilder baggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; return this; }
        public FlightBuilder corporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; return this; }

        public Flight build() {
            Flight obj = new Flight();
            obj.setId(this.id);
            obj.setFlightNumber(this.flightNumber);
            obj.setAirline(this.airline);
            obj.setAirlineCode(this.airlineCode);
            obj.setOriginCode(this.originCode);
            obj.setOriginCity(this.originCity);
            obj.setDestinationCode(this.destinationCode);
            obj.setDestinationCity(this.destinationCity);
            obj.setDepartureTime(this.departureTime);
            obj.setArrivalTime(this.arrivalTime);
            obj.setDurationMinutes(this.durationMinutes);
            obj.setStopsCount(this.stopsCount);
            obj.setTravelClass(this.travelClass);
            obj.setPrice(this.price);
            obj.setCurrencyCode(this.currencyCode);
            obj.setCarbonEmissionsKg(this.carbonEmissionsKg);
            obj.setRefundable(this.refundable);
            obj.setBaggageAllowance(this.baggageAllowance);
            obj.setCorporatePreferred(this.corporatePreferred);
            return obj;
        }
    }
}
