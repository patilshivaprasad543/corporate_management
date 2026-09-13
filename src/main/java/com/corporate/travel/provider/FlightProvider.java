package com.corporate.travel.provider;

import com.corporate.travel.dto.TravelSearchDto;
import java.util.List;

public interface FlightProvider {
    List<TravelSearchDto.FlightResult> searchFlights(String origin, String destination, String departureDate);
    String getProviderName();
    boolean isLiveIntegration();
}
