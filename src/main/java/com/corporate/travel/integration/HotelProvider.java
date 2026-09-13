package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import java.util.List;

public interface HotelProvider {
    List<TravelSearchDto.HotelResult> searchHotels(TravelSearchDto.HotelSearchCriteria criteria);
    boolean isLiveProvider();
    String getProviderName();
}
