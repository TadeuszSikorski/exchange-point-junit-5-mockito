package pl.sikorski.tadeusz;

public class IncorrectExchangeRatesException extends Exception {

    IncorrectExchangeRatesException() {
        super("Incorrect exchange rates.");
    }
}
