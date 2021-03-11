package pl.sikorski.tadeusz;

import java.math.BigDecimal;

public interface ExchangeRatesProvider {
    public BigDecimal getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency);
}
