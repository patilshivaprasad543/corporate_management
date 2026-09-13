package com.corporate.travel.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.corporate.travel.dto.TravelSearchDto;
import java.util.List;

public interface TransportationProvider {
    List<TravelSearchDto.TransportResult> searchTransport(String origin, String destination);
    boolean isLiveProvider();
}
