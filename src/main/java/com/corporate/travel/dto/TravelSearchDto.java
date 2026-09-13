package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import com.corporate.travel.entity.enums.TravelClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TravelSearchDto {

    public static class FlightSearchCriteria {
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private TravelClass travelClass;
        private Integer travelers;
        private BigDecimal maxPrice;
        private Boolean directOnly;
        private String preferredAirline;

    public FlightSearchCriteria() {}

    public FlightSearchCriteria(String origin, String destination, LocalDate departureDate, LocalDate returnDate, TravelClass travelClass, Integer travelers, BigDecimal maxPrice, Boolean directOnly, String preferredAirline) {
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.travelClass = travelClass;
        this.travelers = travelers;
        this.maxPrice = maxPrice;
        this.directOnly = directOnly;
        this.preferredAirline = preferredAirline;
    }

    public String getOrigin() { return origin; }

    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getDepartureDate() { return departureDate; }

    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public TravelClass getTravelClass() { return travelClass; }

    public void setTravelClass(TravelClass travelClass) { this.travelClass = travelClass; }

    public Integer getTravelers() { return travelers; }

    public void setTravelers(Integer travelers) { this.travelers = travelers; }

    public BigDecimal getMaxPrice() { return maxPrice; }

    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public Boolean isDirectOnly() { return directOnly; }

    public Boolean getDirectOnly() { return directOnly; }

    public void setDirectOnly(Boolean directOnly) { this.directOnly = directOnly; }

    public String getPreferredAirline() { return preferredAirline; }

    public void setPreferredAirline(String preferredAirline) { this.preferredAirline = preferredAirline; }

    public static FlightSearchCriteriaBuilder builder() { return new FlightSearchCriteriaBuilder(); }

    public static class FlightSearchCriteriaBuilder {
        private String origin;
        private String destination;
        private LocalDate departureDate;
        private LocalDate returnDate;
        private TravelClass travelClass;
        private Integer travelers;
        private BigDecimal maxPrice;
        private Boolean directOnly;
        private String preferredAirline;

        public FlightSearchCriteriaBuilder origin(String origin) { this.origin = origin; return this; }
        public FlightSearchCriteriaBuilder destination(String destination) { this.destination = destination; return this; }
        public FlightSearchCriteriaBuilder departureDate(LocalDate departureDate) { this.departureDate = departureDate; return this; }
        public FlightSearchCriteriaBuilder returnDate(LocalDate returnDate) { this.returnDate = returnDate; return this; }
        public FlightSearchCriteriaBuilder travelClass(TravelClass travelClass) { this.travelClass = travelClass; return this; }
        public FlightSearchCriteriaBuilder travelers(Integer travelers) { this.travelers = travelers; return this; }
        public FlightSearchCriteriaBuilder maxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; return this; }
        public FlightSearchCriteriaBuilder directOnly(Boolean directOnly) { this.directOnly = directOnly; return this; }
        public FlightSearchCriteriaBuilder preferredAirline(String preferredAirline) { this.preferredAirline = preferredAirline; return this; }

        public FlightSearchCriteria build() {
            FlightSearchCriteria obj = new FlightSearchCriteria();
            obj.setOrigin(this.origin);
            obj.setDestination(this.destination);
            obj.setDepartureDate(this.departureDate);
            obj.setReturnDate(this.returnDate);
            obj.setTravelClass(this.travelClass);
            obj.setTravelers(this.travelers);
            obj.setMaxPrice(this.maxPrice);
            obj.setDirectOnly(this.directOnly);
            obj.setPreferredAirline(this.preferredAirline);
            return obj;
        }
    }
    }

    public static class FlightResult {
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
        private Integer stopsCount;
        private TravelClass travelClass;
        private BigDecimal price;
        private String currencyCode;
        private BigDecimal carbonEmissionsKg;
        private Boolean refundable;
        private String baggageAllowance;
        private Boolean corporatePreferred;
        private PolicyComplianceStatus complianceStatus;
        private String policyNote;
        private Boolean aiRecommended;
        private String aiReason;

    public FlightResult() {}

    public FlightResult(Long id, String flightNumber, String airline, String airlineCode, String originCode, String originCity, String destinationCode, String destinationCity, LocalDateTime departureTime, LocalDateTime arrivalTime, Integer durationMinutes, Integer stopsCount, TravelClass travelClass, BigDecimal price, String currencyCode, BigDecimal carbonEmissionsKg, Boolean refundable, String baggageAllowance, Boolean corporatePreferred, PolicyComplianceStatus complianceStatus, String policyNote, Boolean aiRecommended, String aiReason) {
        this.id = id;
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
        this.complianceStatus = complianceStatus;
        this.policyNote = policyNote;
        this.aiRecommended = aiRecommended;
        this.aiReason = aiReason;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public PolicyComplianceStatus getComplianceStatus() { return complianceStatus; }

    public void setComplianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; }

    public String getPolicyNote() { return policyNote; }

    public void setPolicyNote(String policyNote) { this.policyNote = policyNote; }

    public Boolean isAiRecommended() { return aiRecommended; }

    public Boolean getAiRecommended() { return aiRecommended; }

    public void setAiRecommended(Boolean aiRecommended) { this.aiRecommended = aiRecommended; }

    public String getAiReason() { return aiReason; }

    public void setAiReason(String aiReason) { this.aiReason = aiReason; }

    public static FlightResultBuilder builder() { return new FlightResultBuilder(); }

    public static class FlightResultBuilder {
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
        private Integer stopsCount;
        private TravelClass travelClass;
        private BigDecimal price;
        private String currencyCode;
        private BigDecimal carbonEmissionsKg;
        private Boolean refundable;
        private String baggageAllowance;
        private Boolean corporatePreferred;
        private PolicyComplianceStatus complianceStatus;
        private String policyNote;
        private Boolean aiRecommended;
        private String aiReason;

        public FlightResultBuilder id(Long id) { this.id = id; return this; }
        public FlightResultBuilder flightNumber(String flightNumber) { this.flightNumber = flightNumber; return this; }
        public FlightResultBuilder airline(String airline) { this.airline = airline; return this; }
        public FlightResultBuilder airlineCode(String airlineCode) { this.airlineCode = airlineCode; return this; }
        public FlightResultBuilder originCode(String originCode) { this.originCode = originCode; return this; }
        public FlightResultBuilder originCity(String originCity) { this.originCity = originCity; return this; }
        public FlightResultBuilder destinationCode(String destinationCode) { this.destinationCode = destinationCode; return this; }
        public FlightResultBuilder destinationCity(String destinationCity) { this.destinationCity = destinationCity; return this; }
        public FlightResultBuilder departureTime(LocalDateTime departureTime) { this.departureTime = departureTime; return this; }
        public FlightResultBuilder arrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; return this; }
        public FlightResultBuilder durationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; return this; }
        public FlightResultBuilder stopsCount(Integer stopsCount) { this.stopsCount = stopsCount; return this; }
        public FlightResultBuilder travelClass(TravelClass travelClass) { this.travelClass = travelClass; return this; }
        public FlightResultBuilder price(BigDecimal price) { this.price = price; return this; }
        public FlightResultBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public FlightResultBuilder carbonEmissionsKg(BigDecimal carbonEmissionsKg) { this.carbonEmissionsKg = carbonEmissionsKg; return this; }
        public FlightResultBuilder refundable(Boolean refundable) { this.refundable = refundable; return this; }
        public FlightResultBuilder baggageAllowance(String baggageAllowance) { this.baggageAllowance = baggageAllowance; return this; }
        public FlightResultBuilder corporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; return this; }
        public FlightResultBuilder complianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; return this; }
        public FlightResultBuilder policyNote(String policyNote) { this.policyNote = policyNote; return this; }
        public FlightResultBuilder aiRecommended(Boolean aiRecommended) { this.aiRecommended = aiRecommended; return this; }
        public FlightResultBuilder aiReason(String aiReason) { this.aiReason = aiReason; return this; }

        public FlightResult build() {
            FlightResult obj = new FlightResult();
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
            obj.setComplianceStatus(this.complianceStatus);
            obj.setPolicyNote(this.policyNote);
            obj.setAiRecommended(this.aiRecommended);
            obj.setAiReason(this.aiReason);
            return obj;
        }
    }
    }

    public static class HotelSearchCriteria {
        private String city;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer rooms;
        private Integer guests;
        private HotelCategory category;
        private BigDecimal maxPrice;

    public HotelSearchCriteria() {}

    public HotelSearchCriteria(String city, LocalDate checkIn, LocalDate checkOut, Integer rooms, Integer guests, HotelCategory category, BigDecimal maxPrice) {
        this.city = city;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rooms = rooms;
        this.guests = guests;
        this.category = category;
        this.maxPrice = maxPrice;
    }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public LocalDate getCheckIn() { return checkIn; }

    public void setCheckIn(LocalDate checkIn) { this.checkIn = checkIn; }

    public LocalDate getCheckOut() { return checkOut; }

    public void setCheckOut(LocalDate checkOut) { this.checkOut = checkOut; }

    public Integer getRooms() { return rooms; }

    public void setRooms(Integer rooms) { this.rooms = rooms; }

    public Integer getGuests() { return guests; }

    public void setGuests(Integer guests) { this.guests = guests; }

    public HotelCategory getCategory() { return category; }

    public void setCategory(HotelCategory category) { this.category = category; }

    public BigDecimal getMaxPrice() { return maxPrice; }

    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }

    public static HotelSearchCriteriaBuilder builder() { return new HotelSearchCriteriaBuilder(); }

    public static class HotelSearchCriteriaBuilder {
        private String city;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer rooms;
        private Integer guests;
        private HotelCategory category;
        private BigDecimal maxPrice;

        public HotelSearchCriteriaBuilder city(String city) { this.city = city; return this; }
        public HotelSearchCriteriaBuilder checkIn(LocalDate checkIn) { this.checkIn = checkIn; return this; }
        public HotelSearchCriteriaBuilder checkOut(LocalDate checkOut) { this.checkOut = checkOut; return this; }
        public HotelSearchCriteriaBuilder rooms(Integer rooms) { this.rooms = rooms; return this; }
        public HotelSearchCriteriaBuilder guests(Integer guests) { this.guests = guests; return this; }
        public HotelSearchCriteriaBuilder category(HotelCategory category) { this.category = category; return this; }
        public HotelSearchCriteriaBuilder maxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; return this; }

        public HotelSearchCriteria build() {
            HotelSearchCriteria obj = new HotelSearchCriteria();
            obj.setCity(this.city);
            obj.setCheckIn(this.checkIn);
            obj.setCheckOut(this.checkOut);
            obj.setRooms(this.rooms);
            obj.setGuests(this.guests);
            obj.setCategory(this.category);
            obj.setMaxPrice(this.maxPrice);
            return obj;
        }
    }
    }

    public static class HotelResult {
        private Long id;
        private String name;
        private String city;
        private String address;
        private Double starRating;
        private HotelCategory category;
        private String roomType;
        private BigDecimal pricePerNight;
        private String currencyCode;
        private String amenities;
        private String imageUrl;
        private Boolean corporatePreferred;
        private Boolean freeCancellation;
        private PolicyComplianceStatus complianceStatus;
        private String policyNote;
        private Boolean aiRecommended;

    public HotelResult() {}

    public HotelResult(Long id, String name, String city, String address, Double starRating, HotelCategory category, String roomType, BigDecimal pricePerNight, String currencyCode, String amenities, String imageUrl, Boolean corporatePreferred, Boolean freeCancellation, PolicyComplianceStatus complianceStatus, String policyNote, Boolean aiRecommended) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.starRating = starRating;
        this.category = category;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.currencyCode = currencyCode;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
        this.corporatePreferred = corporatePreferred;
        this.freeCancellation = freeCancellation;
        this.complianceStatus = complianceStatus;
        this.policyNote = policyNote;
        this.aiRecommended = aiRecommended;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Double getStarRating() { return starRating; }

    public void setStarRating(Double starRating) { this.starRating = starRating; }

    public HotelCategory getCategory() { return category; }

    public void setCategory(HotelCategory category) { this.category = category; }

    public String getRoomType() { return roomType; }

    public void setRoomType(String roomType) { this.roomType = roomType; }

    public BigDecimal getPricePerNight() { return pricePerNight; }

    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public String getAmenities() { return amenities; }

    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getImageUrl() { return imageUrl; }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean isCorporatePreferred() { return corporatePreferred; }

    public Boolean getCorporatePreferred() { return corporatePreferred; }

    public void setCorporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; }

    public Boolean isFreeCancellation() { return freeCancellation; }

    public Boolean getFreeCancellation() { return freeCancellation; }

    public void setFreeCancellation(Boolean freeCancellation) { this.freeCancellation = freeCancellation; }

    public PolicyComplianceStatus getComplianceStatus() { return complianceStatus; }

    public void setComplianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; }

    public String getPolicyNote() { return policyNote; }

    public void setPolicyNote(String policyNote) { this.policyNote = policyNote; }

    public Boolean isAiRecommended() { return aiRecommended; }

    public Boolean getAiRecommended() { return aiRecommended; }

    public void setAiRecommended(Boolean aiRecommended) { this.aiRecommended = aiRecommended; }

    public static HotelResultBuilder builder() { return new HotelResultBuilder(); }

    public static class HotelResultBuilder {
        private Long id;
        private String name;
        private String city;
        private String address;
        private Double starRating;
        private HotelCategory category;
        private String roomType;
        private BigDecimal pricePerNight;
        private String currencyCode;
        private String amenities;
        private String imageUrl;
        private Boolean corporatePreferred;
        private Boolean freeCancellation;
        private PolicyComplianceStatus complianceStatus;
        private String policyNote;
        private Boolean aiRecommended;

        public HotelResultBuilder id(Long id) { this.id = id; return this; }
        public HotelResultBuilder name(String name) { this.name = name; return this; }
        public HotelResultBuilder city(String city) { this.city = city; return this; }
        public HotelResultBuilder address(String address) { this.address = address; return this; }
        public HotelResultBuilder starRating(Double starRating) { this.starRating = starRating; return this; }
        public HotelResultBuilder category(HotelCategory category) { this.category = category; return this; }
        public HotelResultBuilder roomType(String roomType) { this.roomType = roomType; return this; }
        public HotelResultBuilder pricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; return this; }
        public HotelResultBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public HotelResultBuilder amenities(String amenities) { this.amenities = amenities; return this; }
        public HotelResultBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public HotelResultBuilder corporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; return this; }
        public HotelResultBuilder freeCancellation(Boolean freeCancellation) { this.freeCancellation = freeCancellation; return this; }
        public HotelResultBuilder complianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; return this; }
        public HotelResultBuilder policyNote(String policyNote) { this.policyNote = policyNote; return this; }
        public HotelResultBuilder aiRecommended(Boolean aiRecommended) { this.aiRecommended = aiRecommended; return this; }

        public HotelResult build() {
            HotelResult obj = new HotelResult();
            obj.setId(this.id);
            obj.setName(this.name);
            obj.setCity(this.city);
            obj.setAddress(this.address);
            obj.setStarRating(this.starRating);
            obj.setCategory(this.category);
            obj.setRoomType(this.roomType);
            obj.setPricePerNight(this.pricePerNight);
            obj.setCurrencyCode(this.currencyCode);
            obj.setAmenities(this.amenities);
            obj.setImageUrl(this.imageUrl);
            obj.setCorporatePreferred(this.corporatePreferred);
            obj.setFreeCancellation(this.freeCancellation);
            obj.setComplianceStatus(this.complianceStatus);
            obj.setPolicyNote(this.policyNote);
            obj.setAiRecommended(this.aiRecommended);
            return obj;
        }
    }
    }

    public static class TransportResult {
        private Long id;
        private String providerName;
        private String transportType;
        private String pickupLocation;
        private String dropLocation;
        private LocalDateTime pickupTime;
        private String vehicleModel;
        private BigDecimal price;
        private String currencyCode;
        private PolicyComplianceStatus complianceStatus;

    public TransportResult() {}

    public TransportResult(Long id, String providerName, String transportType, String pickupLocation, String dropLocation, LocalDateTime pickupTime, String vehicleModel, BigDecimal price, String currencyCode, PolicyComplianceStatus complianceStatus) {
        this.id = id;
        this.providerName = providerName;
        this.transportType = transportType;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.pickupTime = pickupTime;
        this.vehicleModel = vehicleModel;
        this.price = price;
        this.currencyCode = currencyCode;
        this.complianceStatus = complianceStatus;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public PolicyComplianceStatus getComplianceStatus() { return complianceStatus; }

    public void setComplianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; }

    public static TransportResultBuilder builder() { return new TransportResultBuilder(); }

    public static class TransportResultBuilder {
        private Long id;
        private String providerName;
        private String transportType;
        private String pickupLocation;
        private String dropLocation;
        private LocalDateTime pickupTime;
        private String vehicleModel;
        private BigDecimal price;
        private String currencyCode;
        private PolicyComplianceStatus complianceStatus;

        public TransportResultBuilder id(Long id) { this.id = id; return this; }
        public TransportResultBuilder providerName(String providerName) { this.providerName = providerName; return this; }
        public TransportResultBuilder transportType(String transportType) { this.transportType = transportType; return this; }
        public TransportResultBuilder pickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; return this; }
        public TransportResultBuilder dropLocation(String dropLocation) { this.dropLocation = dropLocation; return this; }
        public TransportResultBuilder pickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; return this; }
        public TransportResultBuilder vehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; return this; }
        public TransportResultBuilder price(BigDecimal price) { this.price = price; return this; }
        public TransportResultBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public TransportResultBuilder complianceStatus(PolicyComplianceStatus complianceStatus) { this.complianceStatus = complianceStatus; return this; }

        public TransportResult build() {
            TransportResult obj = new TransportResult();
            obj.setId(this.id);
            obj.setProviderName(this.providerName);
            obj.setTransportType(this.transportType);
            obj.setPickupLocation(this.pickupLocation);
            obj.setDropLocation(this.dropLocation);
            obj.setPickupTime(this.pickupTime);
            obj.setVehicleModel(this.vehicleModel);
            obj.setPrice(this.price);
            obj.setCurrencyCode(this.currencyCode);
            obj.setComplianceStatus(this.complianceStatus);
            return obj;
        }
    }
    }


    public TravelSearchDto() {}

    public static TravelSearchDtoBuilder builder() { return new TravelSearchDtoBuilder(); }

    public static class TravelSearchDtoBuilder {


        public TravelSearchDto build() {
            TravelSearchDto obj = new TravelSearchDto();
            return obj;
        }
    }
}
