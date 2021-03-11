package pl.sikorski.tadeusz;

import java.util.Objects;

public class CurrencyPair {

    private CurrencyCode fromCurrency;
    private CurrencyCode toCurrency;
    
    public CurrencyPair(CurrencyCode from, CurrencyCode to) {
        this.fromCurrency = from;
        this.toCurrency = to;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        
        if (object == null || getClass() != object.getClass()) return false;
        
        CurrencyPair currencyPair = (CurrencyPair) object;
        
        return this.fromCurrency == currencyPair.fromCurrency 
            && this.toCurrency == currencyPair.toCurrency;
    }

        @Override
        public int hashCode() {
            return Objects.hash(this.fromCurrency, this.toCurrency);
        }
}