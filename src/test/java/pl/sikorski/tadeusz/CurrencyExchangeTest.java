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

    @ParameterizedTest
    @ValueSource(strings = {"0.00", "-0.01", "-1.00"})
    void spreadEqualToOrLessThanZero(String spread) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal(spread), exchangeRatesProvider);
            });

        assertEquals("Spread must be greater than zero and less than one.", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"1.00","5.00"})
    void spreadEqualToOrGreaterThanOne(String spread) {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal(spread), exchangeRatesProvider);
            });

        assertEquals("Spread must be greater than zero and less than one.", exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

                currencyExchange.getBidPrice(new BigDecimal("0.00"), CurrencyCode.EUR, CurrencyCode.PLN);
            });
        
        assertEquals("Amount of money must be greater than zero.", exception.getMessage());
    }

    @Test
    void askPriceForSpecifiedZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

                currencyExchange.getAskPrice(new BigDecimal("0.00"), CurrencyCode.EUR, CurrencyCode.PLN);
            });
        
        assertEquals("Amount of money must be greater than zero.", exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedLessThanZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

                currencyExchange.getBidPrice(new BigDecimal("-1.00"), CurrencyCode.EUR, CurrencyCode.PLN);
            });

        assertEquals("Amount of money must be greater than zero.", exception.getMessage());
    }

    @Test
    void askPriceForSpecifiedLessThanZeroAmountOfMoneyEURtoPLN() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

                currencyExchange.getAskPrice(new BigDecimal("-1.00"), CurrencyCode.EUR, CurrencyCode.PLN);
            });

        assertEquals("Amount of money must be greater than zero.", exception.getMessage());
    }

    @Test
    void bidPriceForSpecifiedSpreadEURtoPLN() {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.PLN)).thenReturn(new BigDecimal("4.5540"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("454.4000"), currencyExchange.getBidPrice(new BigDecimal("100.00"), CurrencyCode.EUR, CurrencyCode.PLN));
    }

    @Test
    void askPriceForSpecifiedSpreadEURtoPLN() {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.EUR, CurrencyCode.PLN)).thenReturn(new BigDecimal("4.5540"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("456.4000"), currencyExchange.getAskPrice(new BigDecimal("100.00"), CurrencyCode.EUR, CurrencyCode.PLN));
    }

    @Test
    void bidPriceForSpecifiedSpreadPLNtoEUR() {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.PLN, CurrencyCode.EUR)).thenReturn(new BigDecimal("0.2196"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("20.9600"), currencyExchange.getBidPrice(new BigDecimal("100.00"), CurrencyCode.PLN, CurrencyCode.EUR));
    }

    @Test
    void askPriceForSpecifiedSpreadPLNtoEUR() {
        when(exchangeRatesProvider.getExchangeRate(CurrencyCode.PLN, CurrencyCode.EUR)).thenReturn(new BigDecimal("0.2196"));
        CurrencyExchange currencyExchange = new CurrencyExchange(new BigDecimal("0.0200"), exchangeRatesProvider);

        assertEquals(new BigDecimal("22.9600"), currencyExchange.getAskPrice(new BigDecimal("100.00"), CurrencyCode.PLN, CurrencyCode.EUR));
    }
}
