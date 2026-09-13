package com.corporate.travel.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.integration.FlightProvider;
import com.corporate.travel.integration.HotelProvider;
import com.corporate.travel.integration.TransportationProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelSearchService {
    private static final Logger log = LoggerFactory.getLogger(TravelSearchService.class);

    public TravelSearchService(FlightProvider flightProvider, HotelProvider hotelProvider, TransportationProvider transportationProvider) {
        this.flightProvider = flightProvider;
        this.hotelProvider = hotelProvider;
        this.transportationProvider = transportationProvider;
    }


    private final FlightProvider flightProvider;
    private final HotelProvider hotelProvider;
    private final TransportationProvider transportationProvider;

    public List<TravelSearchDto.FlightResult> searchFlights(TravelSearchDto.FlightSearchCriteria criteria) {
        return flightProvider.searchFlights(criteria);
    }

    public List<TravelSearchDto.HotelResult> searchHotels(TravelSearchDto.HotelSearchCriteria criteria) {
        return hotelProvider.searchHotels(criteria);
    }

    public List<TravelSearchDto.TransportResult> searchTransport(String origin, String destination) {
        return transportationProvider.searchTransport(origin, destination);
    }
}
