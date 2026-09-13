package com.corporate.travel.entity;

import com.corporate.travel.common.BaseEntity;
import com.corporate.travel.entity.enums.HotelCategory;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "hotels")

public class Hotel extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(length = 255)
    private String address;

    @Column(name = "star_rating")
    private Double starRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private HotelCategory category;

    @Column(name = "room_type", length = 100)
    private String roomType;

    @Column(name = "price_per_night", precision = 12, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "currency_code", length = 10)

    private String currencyCode = "INR";

    @Column(name = "amenities", length = 500)
    private String amenities;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_corporate_preferred")

    private Boolean corporatePreferred = false;

    @Column(name = "free_cancellation")

    private Boolean freeCancellation = true;


    public Hotel() {}

    public Hotel(String name, String city, String address, Double starRating, HotelCategory category, String roomType, BigDecimal pricePerNight, String currencyCode, String amenities, String imageUrl, Boolean corporatePreferred, Boolean freeCancellation) {
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
    }

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

    public static HotelBuilder builder() { return new HotelBuilder(); }

    public static class HotelBuilder {
        private Long id;
        private String name;
        private String city;
        private String address;
        private Double starRating;
        private HotelCategory category;
        private String roomType;
        private BigDecimal pricePerNight;
        private String currencyCode = "INR";
        private String amenities;
        private String imageUrl;
        private Boolean corporatePreferred = false;
        private Boolean freeCancellation = true;

        public HotelBuilder id(Long id) { this.id = id; return this; }
        public HotelBuilder name(String name) { this.name = name; return this; }
        public HotelBuilder city(String city) { this.city = city; return this; }
        public HotelBuilder address(String address) { this.address = address; return this; }
        public HotelBuilder starRating(Double starRating) { this.starRating = starRating; return this; }
        public HotelBuilder category(HotelCategory category) { this.category = category; return this; }
        public HotelBuilder roomType(String roomType) { this.roomType = roomType; return this; }
        public HotelBuilder pricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; return this; }
        public HotelBuilder currencyCode(String currencyCode) { this.currencyCode = currencyCode; return this; }
        public HotelBuilder amenities(String amenities) { this.amenities = amenities; return this; }
        public HotelBuilder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public HotelBuilder corporatePreferred(Boolean corporatePreferred) { this.corporatePreferred = corporatePreferred; return this; }
        public HotelBuilder freeCancellation(Boolean freeCancellation) { this.freeCancellation = freeCancellation; return this; }

        public Hotel build() {
            Hotel obj = new Hotel();
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
            return obj;
        }
    }
}
