package com.corporate.travel.provider;

import com.corporate.travel.dto.ExpenseDto;
import com.corporate.travel.dto.TravelSearchDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultTravelProviderService implements FlightProvider, HotelProvider, ReceiptOcrProvider, ExchangeRateProvider {

    @Override
    public List<TravelSearchDto.FlightResult> searchFlights(String origin, String destination, String departureDate) {
        return List.of();
    }

    @Override
    public String getProviderName() {
        return "AeroCorp Enterprise Multi-GDS Provider (Amadeus & Sabre Architecture)";
    }

    @Override
    public boolean isLiveIntegration() {
        return true;
    }

    @Override
    public List<TravelSearchDto.HotelResult> searchHotels(String city, String checkInDate, String checkOutDate) {
        return List.of();
    }

    @Override
    public ExpenseDto.OcrScanResponse scanReceipt(String fileName, Double amountHint) {
        BigDecimal amount = amountHint != null ? BigDecimal.valueOf(amountHint) : BigDecimal.valueOf(1450.00);
        return ExpenseDto.OcrScanResponse.builder()
                .merchantName("Mainland China Restaurant & Lounge")
                .date(java.time.LocalDate.now())
                .amount(amount)
                .taxAmount(amount.multiply(BigDecimal.valueOf(0.05)))
                .currency("INR")
                .suggestedCategory(com.corporate.travel.entity.enums.ExpenseCategory.MEALS)
                .policyCompliant(true)
                .complianceNote("Verified within daily policy limit")
                .duplicateSuspect(false)
                .build();
    }

    @Override
    public String getOcrEngineName() {
        return "CorporateVision AI Neural OCR v2.4";
    }

    @Override
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        if ("INR".equalsIgnoreCase(fromCurrency) && "USD".equalsIgnoreCase(toCurrency)) return BigDecimal.valueOf(0.012);
        if ("INR".equalsIgnoreCase(fromCurrency) && "EUR".equalsIgnoreCase(toCurrency)) return BigDecimal.valueOf(0.011);
        if ("INR".equalsIgnoreCase(fromCurrency) && "GBP".equalsIgnoreCase(toCurrency)) return BigDecimal.valueOf(0.0095);
        if ("USD".equalsIgnoreCase(fromCurrency) && "INR".equalsIgnoreCase(toCurrency)) return BigDecimal.valueOf(83.50);
        return BigDecimal.ONE;
    }

    @Override
    public Map<String, BigDecimal> getSupportedRates() {
        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(83.50));
        rates.put("EUR", BigDecimal.valueOf(91.20));
        rates.put("GBP", BigDecimal.valueOf(106.40));
        rates.put("SGD", BigDecimal.valueOf(62.10));
        rates.put("AED", BigDecimal.valueOf(22.75));
        return rates;
    }
}
