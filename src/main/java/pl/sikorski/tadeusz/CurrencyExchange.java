package pl.sikorski.tadeusz;

import java.math.BigDecimal;

import java.io.IOException;

import java.math.RoundingMode;

public class CurrencyExchange {
    
    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private BigDecimal spread;
    private ExchangeRatesProvider exchangeRatesProvider;

    CurrencyExchange(BigDecimal spread, ExchangeRatesProvider exchangeRatesProvider) {
        if (spread.compareTo(new BigDecimal(0)) == 0 || spread.compareTo(new BigDecimal(0)) == -1
        || spread.compareTo(new BigDecimal(1)) == 0 || spread.compareTo(new BigDecimal(1)) == 1) {
            throw new IllegalArgumentException("Spread must be greater than zero and less than one.");
        }
        
        this.spread = spread;
        this.exchangeRatesProvider = exchangeRatesProvider;
    }

    BigDecimal getBidPrice(BigDecimal amountOfMoney, CurrencyCode fromCurrency, CurrencyCode toCurrency) 
        throws IOException {
        if (amountOfMoney.compareTo(new BigDecimal(0)) == 0 || amountOfMoney.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("Amount of money must be greater than zero.");
        }

        BigDecimal bid = this.exchangeRatesProvider.getExchangeRate(fromCurrency, toCurrency).subtract(spread.divide(new BigDecimal(2)));

        return amountOfMoney.multiply(bid).setScale(SCALE, ROUNDING_MODE);
    }

    BigDecimal getAskPrice(BigDecimal amountOfMoney, CurrencyCode fromCurrency, CurrencyCode toCurrency) 
        throws IOException {
        if (amountOfMoney.compareTo(new BigDecimal(0)) == 0 || amountOfMoney.compareTo(new BigDecimal(0)) == -1) {
            throw new IllegalArgumentException("Amount of money must be greater than zero.");
        }

        BigDecimal ask = this.exchangeRatesProvider.getExchangeRate(fromCurrency, toCurrency).add(spread.divide(new BigDecimal(2)));

        return amountOfMoney.multiply(ask).setScale(SCALE, ROUNDING_MODE);
    }
}
