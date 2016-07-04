package com.dima.converter.repository;

import com.dima.converter.utils.UrlClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import javax.cache.annotation.CacheKey;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

@Repository
public class OpenExchangeClient implements RatesRepository {

    private static final Logger logger = LoggerFactory.getLogger(OpenExchangeClient.class);

    //TODO check thread safety
    private final ObjectMapper mapper = new ObjectMapper();

    private final String appId;

    private final UrlClient client;

    @Autowired
    public OpenExchangeClient(@Value("${openexchangerates.app.id}")String appId, UrlClient client){
        this.appId = appId;
        this.client = client;
    }

    // essential URL structure is built using constants
    private final static String BASE_URL = "http://openexchangerates.org/api/";
    private static final String LATEST = "latest.json";
    private static final String HISTORICAL = "historical/%04d-%02d-%02d.json";
    private static final String APP_ID = "?app_id=";

    // https://openexchangerates.org/api/latest.json?app_id=YOUR_APP_APP_ID
    private static final String LIVE_URL = BASE_URL + LATEST + APP_ID;
    private static final String HISTORICAL_URL = BASE_URL + HISTORICAL + APP_ID;

    @Cacheable(value="liveRates", unless = "!#result.isEmpty()")
    public Map<String, Double> getLive(){
        return getRates(LIVE_URL + appId);
    }

    @Cacheable(value = "historicalRates", unless = "!#result.isEmpty()")
    public Map<String, Double> getHistorical(@CacheKey LocalDate date){

        return getRates(String.format(HISTORICAL_URL + appId, date.getYear(), date.getMonth(), date.getDayOfMonth()));
    }

    private Map<String, Double> getRates(String url){
        Map<String, Double> rates = new HashMap<>();
        try {
            InputStream stream = client.getResponse(url);
            JsonNode exchangeRates = mapper.readValue(stream, JsonNode.class);

            if (isValidResponse(exchangeRates)){
                rates.putAll(parseRates(exchangeRates));
            } else {
                String error = parseError(exchangeRates);
                logger.error("Exchange rates update failed: " + error);
            }
        } catch (IOException e) {
            logger.error("Exchange rates update failed: " + e.getMessage());
        }
        return rates;
    }

    private String parseError(JsonNode errorObj) {
//        "error": true,
//        "status": 401,
//        "message": "invalid_app_id",
//        "description": "Invalid App ID provided - please sign up at https://openexchangerates.org/signup, or contact support@openexchangerates.org."

        StringBuilder b = new StringBuilder();

        b.append(errorObj.get("status").asInt(0))
                .append(" : ")
                .append(errorObj.get("description").asText("Unknown error"));
        return b.toString();
    }

    private Map<String, Double> parseRates(JsonNode exchangeRates) {
        Map<String, Double> rates = new HashMap<>();
//        timestamp: 1449877801,
//        base: "USD",
//        rates: {
//              AED: 3.672538,
//              ...
//        }

        //Date timestamp = new Date((long)(exchangeRates.get("timestamp").longValue()*1000));
        Iterator<Map.Entry<String, JsonNode>> fieldNames = exchangeRates.get("rates").fields();
        fieldNames.forEachRemaining(e -> rates.put(e.getKey(), e.getValue().doubleValue()));

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        //String formattedDate = dateFormat.format(timeStampDate);
        return rates;
    }

    private boolean isValidResponse(JsonNode exchangeRates) {
        return exchangeRates.get("error") == null;
    }

}
