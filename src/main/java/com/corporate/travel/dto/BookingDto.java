package com.corporate.travel.dto;

import com.corporate.travel.entity.enums.BookingStatus;
import com.corporate.travel.entity.enums.BookingType;
import com.corporate.travel.entity.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class BookingDto {

    public static class CreateBookingRequest {
        private Long travelRequestId;
        @NotNull
        private BookingType bookingType;
        private Long flightId;
        private Long hotelId;
        private Long transportationId;
        private Integer hotelNights;
        @NotNull
        private PaymentMethod paymentMethod;
        private Boolean personalBooking;
        private List<PassengerDto> passengers;

    public CreateBookingRequest() {}

    public CreateBookingRequest(Long travelRequestId, BookingType bookingType, Long flightId, Long hotelId, Long transportationId, Integer hotelNights, PaymentMethod paymentMethod, Boolean personalBooking, List<PassengerDto> passengers) {
        this.travelRequestId = travelRequestId;
        this.bookingType = bookingType;
        this.flightId = flightId;
        this.hotelId = hotelId;
        this.transportationId = transportationId;
        this.hotelNights = hotelNights;
        this.paymentMethod = paymentMethod;
        this.personalBooking = personalBooking;
        this.passengers = passengers;
    }

    public Long getTravelRequestId() { return travelRequestId; }

    public void setTravelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; }

    public BookingType getBookingType() { return bookingType; }

    public void setBookingType(BookingType bookingType) { this.bookingType = bookingType; }

    public Long getFlightId() { return flightId; }

    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public Long getHotelId() { return hotelId; }

    public void setHotelId(Long hotelId) { this.hotelId = hotelId; }

    public Long getTransportationId() { return transportationId; }

    public void setTransportationId(Long transportationId) { this.transportationId = transportationId; }

    public Integer getHotelNights() { return hotelNights; }

    public void setHotelNights(Integer hotelNights) { this.hotelNights = hotelNights; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean isPersonalBooking() { return personalBooking; }

    public Boolean getPersonalBooking() { return personalBooking; }

    public void setPersonalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; }

    public List<PassengerDto> getPassengers() { return passengers; }

    public void setPassengers(List<PassengerDto> passengers) { this.passengers = passengers; }

    public static CreateBookingRequestBuilder builder() { return new CreateBookingRequestBuilder(); }

    public static class CreateBookingRequestBuilder {
        private Long travelRequestId;
        private BookingType bookingType;
        private Long flightId;
        private Long hotelId;
        private Long transportationId;
        private Integer hotelNights;
        private PaymentMethod paymentMethod;
        private Boolean personalBooking;
        private List<PassengerDto> passengers;

        public CreateBookingRequestBuilder travelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; return this; }
        public CreateBookingRequestBuilder bookingType(BookingType bookingType) { this.bookingType = bookingType; return this; }
        public CreateBookingRequestBuilder flightId(Long flightId) { this.flightId = flightId; return this; }
        public CreateBookingRequestBuilder hotelId(Long hotelId) { this.hotelId = hotelId; return this; }
        public CreateBookingRequestBuilder transportationId(Long transportationId) { this.transportationId = transportationId; return this; }
        public CreateBookingRequestBuilder hotelNights(Integer hotelNights) { this.hotelNights = hotelNights; return this; }
        public CreateBookingRequestBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public CreateBookingRequestBuilder personalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; return this; }
        public CreateBookingRequestBuilder passengers(List<PassengerDto> passengers) { this.passengers = passengers; return this; }

        public CreateBookingRequest build() {
            CreateBookingRequest obj = new CreateBookingRequest();
            obj.setTravelRequestId(this.travelRequestId);
            obj.setBookingType(this.bookingType);
            obj.setFlightId(this.flightId);
            obj.setHotelId(this.hotelId);
            obj.setTransportationId(this.transportationId);
            obj.setHotelNights(this.hotelNights);
            obj.setPaymentMethod(this.paymentMethod);
            obj.setPersonalBooking(this.personalBooking);
            obj.setPassengers(this.passengers);
            return obj;
        }
    }
    }

    public static class PassengerDto {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String passportNumber;

    public PassengerDto() {}

    public PassengerDto(String firstName, String lastName, String email, String phone, String passportNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getPassportNumber() { return passportNumber; }

    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public static PassengerDtoBuilder builder() { return new PassengerDtoBuilder(); }

    public static class PassengerDtoBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String passportNumber;

        public PassengerDtoBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public PassengerDtoBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public PassengerDtoBuilder email(String email) { this.email = email; return this; }
        public PassengerDtoBuilder phone(String phone) { this.phone = phone; return this; }
        public PassengerDtoBuilder passportNumber(String passportNumber) { this.passportNumber = passportNumber; return this; }

        public PassengerDto build() {
            PassengerDto obj = new PassengerDto();
            obj.setFirstName(this.firstName);
            obj.setLastName(this.lastName);
            obj.setEmail(this.email);
            obj.setPhone(this.phone);
            obj.setPassportNumber(this.passportNumber);
            return obj;
        }
    }
    }

    public static class Response {
        private Long id;
        private String bookingReference;
        private String pnrNumber;
        private BookingType bookingType;
        private BookingStatus status;
        private Long travelRequestId;
        private String tripName;
        private BigDecimal totalAmount;
        private BigDecimal taxAmount;
        private String currencyCode;
        private PaymentMethod paymentMethod;
        private Boolean personalBooking;
        private String eTicketNumber;
        private LocalDateTime createdAt;
        private List<BookingItemDto> items;

    public Response() {}

    public Response(Long id, String bookingReference, String pnrNumber, BookingType bookingType, BookingStatus status, Long travelRequestId, String tripName, BigDecimal totalAmount, BigDecimal taxAmount, String currencyCode, PaymentMethod paymentMethod, Boolean personalBooking, String eTicketNumber, LocalDateTime createdAt, List<BookingItemDto> items) {
        this.id = id;
        this.bookingReference = bookingReference;
        this.pnrNumber = pnrNumber;
        this.bookingType = bookingType;
        this.status = status;
        this.travelRequestId = travelRequestId;
        this.tripName = tripName;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.currencyCode = currencyCode;
        this.paymentMethod = paymentMethod;
        this.personalBooking = personalBooking;
        this.eTicketNumber = eTicketNumber;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getBookingReference() { return bookingReference; }

    public void setBookingReference(String bookingReference) { this.bookingReference = bookingReference; }

    public String getPnrNumber() { return pnrNumber; }

    public void setPnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; }

    public BookingType getBookingType() { return bookingType; }

    public void setBookingType(BookingType bookingType) { this.bookingType = bookingType; }

    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }

    public Long getTravelRequestId() { return travelRequestId; }

    public void setTravelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; }

    public String getTripName() { return tripName; }

    public void setTripName(String tripName) { this.tripName = tripName; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getTaxAmount() { return taxAmount; }

    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }

    public String getCurrencyCode() { return currencyCode; }

    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }

    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean isPersonalBooking() { return personalBooking; }

    public Boolean getPersonalBooking() { return personalBooking; }

    public void setPersonalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; }

    public String getETicketNumber() { return eTicketNumber; }

    public void setETicketNumber(String eTicketNumber) { this.eTicketNumber = eTicketNumber; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<BookingItemDto> getItems() { return items; }

    public void setItems(List<BookingItemDto> items) { this.items = items; }

    public static ResponseBuilder builder() { return new ResponseBuilder(); }

    public static class ResponseBuilder {
        private Long id;
        private String bookingReference;
        private String pnrNumber;
        private BookingType bookingType;
        private BookingStatus status;
        private Long travelRequestId;
        private String tripName;
        private BigDecimal totalAmount;
        private BigDecimal taxAmount;
        private String currencyCode;
        private PaymentMethod paymentMethod;
        private Boolean personalBooking;
        private String eTicketNumber;
        private LocalDateTime createdAt;
        private List<BookingItemDto> items;

        public ResponseBuilder id(Long id) { this.id = id; return this; }
        public ResponseBuilder bookingReference(String bookingReference) { this.bookingReference = bookingReference; return this; }
        public ResponseBuilder pnrNumber(String pnrNumber) { this.pnrNumber = pnrNumber; return this; }
        public ResponseBuilder bookingType(BookingType bookingType) { this.bookingType = bookingType; return this; }
        public ResponseBuilder status(BookingStatus status) { this.status = status; return this; }
        public ResponseBuilder travelRequestId(Long travelRequestId) { this.travelRequestId = travelRequestId; return this; }
        public ResponseBuilder tripName(String tripName) { this.tripName = tripName; return this; }
        public ResponseBuilder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public ResponseBuilder taxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
        public ResponseBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public ResponseBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public ResponseBuilder personalBooking(Boolean personalBooking) { this.personalBooking = personalBooking; return this; }
        public ResponseBuilder eTicketNumber(String eTicketNumber) { this.eTicketNumber = eTicketNumber; return this; }
        public ResponseBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public ResponseBuilder items(List<BookingItemDto> items) { this.items = items; return this; }

        public Response build() {
            Response obj = new Response();
            obj.setId(this.id);
            obj.setBookingReference(this.bookingReference);
            obj.setPnrNumber(this.pnrNumber);
            obj.setBookingType(this.bookingType);
            obj.setStatus(this.status);
            obj.setTravelRequestId(this.travelRequestId);
            obj.setTripName(this.tripName);
            obj.setTotalAmount(this.totalAmount);
            obj.setTaxAmount(this.taxAmount);
            obj.setCurrencyCode(this.currencyCode);
            obj.setPaymentMethod(this.paymentMethod);
            obj.setPersonalBooking(this.personalBooking);
            obj.setETicketNumber(this.eTicketNumber);
            obj.setCreatedAt(this.createdAt);
            obj.setItems(this.items);
            return obj;
        }
    }
    }

    public static class BookingItemDto {
        private Long id;
        private String itemType;
        private String title;
        private String description;
        private String supplierReference;
        private BigDecimal price;

    public BookingItemDto() {}

    public BookingItemDto(Long id, String itemType, String title, String description, String supplierReference, BigDecimal price) {
        this.id = id;
        this.itemType = itemType;
        this.title = title;
        this.description = description;
        this.supplierReference = supplierReference;
        this.price = price;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getItemType() { return itemType; }

    public void setItemType(String itemType) { this.itemType = itemType; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getSupplierReference() { return supplierReference; }

    public void setSupplierReference(String supplierReference) { this.supplierReference = supplierReference; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public static BookingItemDtoBuilder builder() { return new BookingItemDtoBuilder(); }

    public static class BookingItemDtoBuilder {
        private Long id;
        private String itemType;
        private String title;
        private String description;
        private String supplierReference;
        private BigDecimal price;

        public BookingItemDtoBuilder id(Long id) { this.id = id; return this; }
        public BookingItemDtoBuilder itemType(String itemType) { this.itemType = itemType; return this; }
        public BookingItemDtoBuilder title(String title) { this.title = title; return this; }
        public BookingItemDtoBuilder description(String description) { this.description = description; return this; }
        public BookingItemDtoBuilder supplierReference(String supplierReference) { this.supplierReference = supplierReference; return this; }
        public BookingItemDtoBuilder price(BigDecimal price) { this.price = price; return this; }

        public BookingItemDto build() {
            BookingItemDto obj = new BookingItemDto();
            obj.setId(this.id);
            obj.setItemType(this.itemType);
            obj.setTitle(this.title);
            obj.setDescription(this.description);
            obj.setSupplierReference(this.supplierReference);
            obj.setPrice(this.price);
            return obj;
        }
    }
    }


    public BookingDto() {}

    public static BookingDtoBuilder builder() { return new BookingDtoBuilder(); }

    public static class BookingDtoBuilder {


        public BookingDto build() {
            BookingDto obj = new BookingDto();
            return obj;
        }
    }
}
