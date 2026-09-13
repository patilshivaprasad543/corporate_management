package com.corporate.travel.provider;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeRateProvider {
    BigDecimal getExchangeRate(String fromCurrency, String toCurrency);
    Map<String, BigDecimal> getSupportedRates();
}
