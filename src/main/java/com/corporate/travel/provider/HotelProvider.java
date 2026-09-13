package com.corporate.travel.provider;

import com.corporate.travel.dto.TravelSearchDto;
import java.util.List;

public interface HotelProvider {
    List<TravelSearchDto.HotelResult> searchHotels(String city, String checkInDate, String checkOutDate);
    String getProviderName();
    boolean isLiveIntegration();
}
