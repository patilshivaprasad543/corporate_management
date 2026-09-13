package com.corporate.travel.controller;

import com.corporate.travel.common.ApiResponse;
import com.corporate.travel.dto.TravelSearchDto;
import com.corporate.travel.service.TravelSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Travel Search", description = "Multi-provider flight, hotel, and ground transport search with policy compliance")
public class TravelSearchController {
    public TravelSearchController(TravelSearchService searchService) {
        this.searchService = searchService;
    }


    private final TravelSearchService searchService;

    @PostMapping("/flights/search")
    @Operation(summary = "Search flights with policy checks and AI recommendations")
    public ResponseEntity<ApiResponse<List<TravelSearchDto.FlightResult>>> searchFlights(
            @RequestBody(required = false) TravelSearchDto.FlightSearchCriteria criteria) {
        if (criteria == null) criteria = new TravelSearchDto.FlightSearchCriteria();
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchFlights(criteria), "Flights retrieved"));
    }

    @GetMapping("/flights/search")
    public ResponseEntity<ApiResponse<List<TravelSearchDto.FlightResult>>> searchFlightsGet(
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "destination", required = false) String destination) {
        TravelSearchDto.FlightSearchCriteria criteria = TravelSearchDto.FlightSearchCriteria.builder()
                .origin(origin)
                .destination(destination)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchFlights(criteria), "Flights retrieved"));
    }

    @PostMapping("/hotels/search")
    @Operation(summary = "Search hotels with negotiated corporate rates and amenity filters")
    public ResponseEntity<ApiResponse<List<TravelSearchDto.HotelResult>>> searchHotels(
            @RequestBody(required = false) TravelSearchDto.HotelSearchCriteria criteria) {
        if (criteria == null) criteria = new TravelSearchDto.HotelSearchCriteria();
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchHotels(criteria), "Hotels retrieved"));
    }

    @GetMapping("/hotels/search")
    public ResponseEntity<ApiResponse<List<TravelSearchDto.HotelResult>>> searchHotelsGet(
            @RequestParam(name = "city", required = false) String city) {
        TravelSearchDto.HotelSearchCriteria criteria = TravelSearchDto.HotelSearchCriteria.builder()
                .city(city)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchHotels(criteria), "Hotels retrieved"));
    }

    @GetMapping("/transport/search")
    @Operation(summary = "Search corporate cabs, rental cars, and train connections")
    public ResponseEntity<ApiResponse<List<TravelSearchDto.TransportResult>>> searchTransport(
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "destination", required = false) String destination) {
        return ResponseEntity.ok(ApiResponse.ok(searchService.searchTransport(origin, destination), "Transport options retrieved"));
    }

}
