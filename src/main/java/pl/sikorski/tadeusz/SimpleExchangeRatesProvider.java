package pl.sikorski.tadeusz;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class SimpleExchangeRatesProvider implements ExchangeRatesProvider {
    
    private final Map<CurrencyPair, BigDecimal> rates = new HashMap<>();

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    public void setRate(CurrencyCode fromCurrency, CurrencyCode toCurrency, BigDecimal rate) throws IncorrectExchangeRatesException {
        
        if(rate.setScale(SCALE, ROUNDING_MODE).compareTo(new BigDecimal("0.0000")) > 0) {
            rates.put(new CurrencyPair(fromCurrency, toCurrency), rate.setScale(SCALE, ROUNDING_MODE));

            rates.put(new CurrencyPair(toCurrency, fromCurrency), BigDecimal.ONE.divide(rate, SCALE, ROUNDING_MODE));
        } 
        else {
            throw new IncorrectExchangeRatesException();
        } 
    }

    @Override
    public BigDecimal getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        CurrencyPair currencyPair = new CurrencyPair(fromCurrency, toCurrency);

        if (!rates.containsKey(currencyPair)) {
            throw new IllegalArgumentException("No exchange rate was found for the specified currency pair.");
        }

        return rates.get(currencyPair);
    }

}
