package pl.sikorski.tadeusz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeTest {
    
    @Mock
    ExchangeRatesProvider exchangeRatesProvider;

    @Mock
    CurrencyExchange currencyExchange;

    @ParameterizedTest
    @ValueSource(strings = {"0.00", "-0.01", "-1.00"})
    void spreadEqualToOrLessThanZero(String spread) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CurrencyExchange(new BigDecimal(spread), exchangeRatesProvider);
        });
        
        fail(exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1.00","5.00"})
    void spreadEqualToOrGreaterThanOne(String spread) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new CurrencyExchange(new BigDecimal(spread), exchangeRatesProvider);
        });
        
        fail(exception.getMessage());
    }

    @Test
    void exchangeRateEqualToZero() {
        IncorrectExchangeRatesException exception = assertThrows(IncorrectExchangeRatesException.class, () -> {
            when(exchangeRatesProvider.getExchangeRate(any(CurrencyCode.class), any(CurrencyCode.class))).thenReturn(new BigDecimal("0.0"));
        });
        
        fail(exception.getMessage());
    }

    @Test
    void exchangeRateLessThanZero() {
        IncorrectExchangeRatesException exception = assertThrows(IncorrectExchangeRatesException.class, () -> {
            when(exchangeRatesProvider.getExchangeRate(any(CurrencyCode.class), any(CurrencyCode.class))).thenReturn(new BigDecimal("-1.0"));
        });
        
        fail(exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyExchange.getBidPrice(new BigDecimal("0.00"), CurrencyCode.EUR, CurrencyCode.PLN);
        });
        
        fail(exception.getMessage());
    }

    @Test
    void askPriceForSpecifiedZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyExchange.getAskPrice(new BigDecimal("0.00"), CurrencyCode.EUR, CurrencyCode.PLN);
        });
        
        fail(exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedLessThanZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyExchange.getBidPrice(new BigDecimal("-1.00"), CurrencyCode.EUR, CurrencyCode.PLN);
        });
        
        fail(exception.getMessage());
    }

    @Test
    void askPriceForSpecifiedLessThanZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            currencyExchange.getAskPrice(new BigDecimal("-1.00"), CurrencyCode.EUR, CurrencyCode.PLN);
        });
        
        fail(exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedSpreadEURtoPLN() throws IOException {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.PLN)).thenReturn(new BigDecimal("4.5540"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("454.40"), currencyExchange.getBidPrice(new BigDecimal("100.00"), CurrencyCode.EUR, CurrencyCode.PLN));
    }

    @Test
    void askPriceForSpecifiedSpreadEURtoPLN() throws IOException {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.PLN)).thenReturn(new BigDecimal("4.5540"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("453.40"), currencyExchange.getAskPrice(new BigDecimal("100.00"), CurrencyCode.EUR, CurrencyCode.PLN));
    }

    @Test
    void bidPriceForSpecifiedSpreadPLNtoEUR() throws IOException {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.PLN, CurrencyCode.EUR)).thenReturn(new BigDecimal("0.2196"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("20.96"), currencyExchange.getBidPrice(new BigDecimal("100.00"), CurrencyCode.PLN, CurrencyCode.EUR));
    }

    @Test
    void askPriceForSpecifiedSpreadPLNtoEUR() throws IOException {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.PLN, CurrencyCode.EUR)).thenReturn(new BigDecimal("0.2196"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("22.96"), currencyExchange.getAskPrice(new BigDecimal("100.00"), CurrencyCode.PLN, CurrencyCode.EUR));
    }
}
