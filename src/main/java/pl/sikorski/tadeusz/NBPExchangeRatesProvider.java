package pl.sikorski.tadeusz;

import java.math.BigDecimal;

import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.math.RoundingMode;
import java.math.MathContext;

public class NBPExchangeRatesProvider implements ExchangeRatesProvider {
    
    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
    private LocalDateTime now = LocalDateTime.now();
    private String actualDate = dateFormat.format(now).toString();

    private static final int SCALE = 4;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    private final SimpleExchangeRatesProvider simpleExchangeRatesProvider = new SimpleExchangeRatesProvider();

    private static String readAll(Reader reader) throws IOException {
        StringBuilder data = new StringBuilder();
        int characterCode;
        
        while ((characterCode = reader.read()) != -1) {
            data.append((char) characterCode);
        }
        
        return data.toString();
    }
    
    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream input = new URL(url).openStream();
        
        try {
          BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
          String jsonText = readAll(reader);
          JSONObject json = new JSONObject(jsonText);
          
          return json;
        
        } finally {
            input.close();
        }
    }

    private BigDecimal getRate(CurrencyCode code) throws IOException {
        JSONObject json = readJsonFromUrl("http://api.nbp.pl/api/exchangerates/rates/a/" 
            + code +"/" + this.actualDate + "/?format=json");

        JSONArray rates = json.getJSONArray("rates");
        JSONObject rate = (JSONObject) rates.get(0);

        String midRate = (String) rate.get("mid");

        return new BigDecimal(midRate);
    }

    private void getExchangeRates() throws IOException, IncorrectExchangeRatesException {
        simpleExchangeRatesProvider.setRate(CurrencyCode.EUR, CurrencyCode.PLN, getRate(CurrencyCode.EUR));
        simpleExchangeRatesProvider.setRate(CurrencyCode.USD, CurrencyCode.PLN, getRate(CurrencyCode.USD));
        
        BigDecimal usdToEUR = getRate(CurrencyCode.EUR).divide(getRate(CurrencyCode.USD));
        
        simpleExchangeRatesProvider.setRate(CurrencyCode.USD, CurrencyCode.EUR, usdToEUR);
    }

    @Override
    public BigDecimal getExchangeRate(CurrencyCode fromCurrency, CurrencyCode toCurrency) {
        try {
            getExchangeRates();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (IncorrectExchangeRatesException exception) {
            exception.printStackTrace();
        }

        return simpleExchangeRatesProvider.getExchangeRate(fromCurrency, toCurrency); 
    }
}
