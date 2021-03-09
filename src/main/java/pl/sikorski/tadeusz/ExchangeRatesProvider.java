package pl.sikorski.tadeusz;

import java.math.BigDecimal;
import java.io.IOException;

public interface ExchangeRatesProvider {
    public BigDecimal getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) throws IOException;
}
