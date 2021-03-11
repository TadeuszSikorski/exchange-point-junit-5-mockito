package pl.sikorski.tadeusz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SimpleExchangeRatesProviderTest {

    private SimpleExchangeRatesProvider exchangeRatesProvider;

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    @BeforeEach
    void initializeExchangeRatesProvider() {
        exchangeRatesProvider = new SimpleExchangeRatesProvider();
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.0000", "-0.0001", "-1.0000"})
    void exchangeRateEqualToOrLessThanZero(String rate) throws IncorrectExchangeRatesException {
        IncorrectExchangeRatesException exception = assertThrows(
            IncorrectExchangeRatesException.class, 
            () -> {
                exchangeRatesProvider.setRate(CurrencyCode.PLN, CurrencyCode.EUR, new BigDecimal(rate)); 
            });

            assertEquals("Incorrect exchange rates.", exception.getMessage());
    }
    
    @Test
    void shouldBeValidRate() throws IncorrectExchangeRatesException {
        exchangeRatesProvider.setRate(CurrencyCode.EUR, CurrencyCode.PLN, new BigDecimal("4.5023"));
        
        assertEquals(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.PLN), new BigDecimal("4.5023"));
    }

    @Test
    void shouldBeValidReversedRate() throws IncorrectExchangeRatesException {
        exchangeRatesProvider.setRate(CurrencyCode.EUR, CurrencyCode.PLN, new BigDecimal("4.5023"));
        
        assertEquals(exchangeRatesProvider.getExchangeRate(CurrencyCode.PLN, CurrencyCode.EUR), new BigDecimal("0.2221"));
    }

    @Test
    void shouldBeValidRateUSDEUR() throws IncorrectExchangeRatesException {
        BigDecimal usdToEURRate = new BigDecimal("4.5023").divide(new BigDecimal("3.7117"), SCALE, ROUNDING_MODE);        
        exchangeRatesProvider.setRate(CurrencyCode.USD, CurrencyCode.EUR, usdToEURRate);
        
        assertEquals(exchangeRatesProvider.getExchangeRate(CurrencyCode.USD, CurrencyCode.EUR), new BigDecimal("1.2130"));
    }

    @Test
    void shouldBeValidReversedRateUSDEUR() throws IncorrectExchangeRatesException {
        BigDecimal usdToEURRate = new BigDecimal("4.5023").divide(new BigDecimal("3.7117"), SCALE, ROUNDING_MODE);
        exchangeRatesProvider.setRate(CurrencyCode.USD, CurrencyCode.EUR, usdToEURRate);
        
        assertEquals(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.USD), new BigDecimal("0.8244"));
    }
    
}