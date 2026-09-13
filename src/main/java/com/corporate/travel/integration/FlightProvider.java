package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import java.util.List;

public interface FlightProvider {
    List<TravelSearchDto.FlightResult> searchFlights(TravelSearchDto.FlightSearchCriteria criteria);
    boolean isLiveProvider();
    String getProviderName();
}
