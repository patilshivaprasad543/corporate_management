package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.entity.enums.HotelCategory;
import com.corporate.travel.entity.enums.PolicyComplianceStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class MockHotelProvider implements HotelProvider {
    private static final Logger log = LoggerFactory.getLogger(MockHotelProvider.class);


    @Override
    public List<TravelSearchDto.HotelResult> searchHotels(TravelSearchDto.HotelSearchCriteria criteria) {
        List<TravelSearchDto.HotelResult> results = new ArrayList<>();
        String city = (criteria.getCity() != null && !criteria.getCity().isBlank()) ? criteria.getCity() : "Delhi";

        results.add(TravelSearchDto.HotelResult.builder()
                .id(201L)
                .name("Taj Palace & Executive Suites")
                .city(city)
                .address("Diplomatic Enclave, Chanakyapuri")
                .starRating(4.8)
                .category(HotelCategory.LUXURY_5_STAR)
                .roomType("Deluxe King Room (Corporate Rate)")
                .pricePerNight(BigDecimal.valueOf(7500))
                .currencyCode("INR")
                .amenities("High-Speed WiFi, Executive Breakfast, Airport Shuttle, Business Center, Fitness Gym")
                .imageUrl("https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600&auto=format&fit=crop&q=80")
                .corporatePreferred(true)
                .freeCancellation(true)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Corporate negotiated rate with complimentary breakfast & late check-out")
                .aiRecommended(true)
                .build());

        results.add(TravelSearchDto.HotelResult.builder()
                .id(202L)
                .name("Courtyard by Marriott Downtown")
                .city(city)
                .address("Commercial Tech Park Sector 29")
                .starRating(4.5)
                .category(HotelCategory.PREMIUM_4_STAR)
                .roomType("Executive Studio Room")
                .pricePerNight(BigDecimal.valueOf(5200))
                .currencyCode("INR")
                .amenities("Free WiFi, Buffet Breakfast, 24/7 Room Service, Conference Room")
                .imageUrl("https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600&auto=format&fit=crop&q=80")
                .corporatePreferred(true)
                .freeCancellation(true)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Within daily corporate hotel budget of ₹6,000")
                .aiRecommended(false)
                .build());

        results.add(TravelSearchDto.HotelResult.builder()
                .id(203L)
                .name("Lemon Tree Premier Business Hotel")
                .city(city)
                .address("Aerocity Hospitality District")
                .starRating(4.2)
                .category(HotelCategory.STANDARD_3_STAR)
                .roomType("Superior Queen Room")
                .pricePerNight(BigDecimal.valueOf(3800))
                .currencyCode("INR")
                .amenities("Free WiFi, Breakfast, Airport Drop, Swimming Pool")
                .imageUrl("https://images.unsplash.com/photo-1590490360182-c33d57733427?w=600&auto=format&fit=crop&q=80")
                .corporatePreferred(true)
                .freeCancellation(true)
                .complianceStatus(PolicyComplianceStatus.COMPLIANT)
                .policyNote("Economical corporate rate within 10 mins of airport")
                .aiRecommended(false)
                .build());

        results.add(TravelSearchDto.HotelResult.builder()
                .id(204L)
                .name("The Grand Heritage Royal Resort")
                .city(city)
                .address("Vasant Kunj Heritage Boulevard")
                .starRating(4.9)
                .category(HotelCategory.LUXURY_5_STAR)
                .roomType("Presidential Suite")
                .pricePerNight(BigDecimal.valueOf(18500))
                .currencyCode("INR")
                .amenities("Butler Service, Private Lounge, Spa, Gourmet Dining")
                .imageUrl("https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600&auto=format&fit=crop&q=80")
                .corporatePreferred(false)
                .freeCancellation(false)
                .complianceStatus(PolicyComplianceStatus.POLICY_VIOLATION)
                .policyNote("Violation: Daily room rate exceeds corporate limit of ₹6,000 by ₹12,500. Requires Executive Admin approval.")
                .aiRecommended(false)
                .build());

        return results;
    }

    @Override
    public boolean isLiveProvider() { return false; }

    @Override
    public String getProviderName() { return "Enterprise Hotel Inventory Mock Adapter"; }
}
