package com.dima.converter.service;

import com.dima.converter.utils.UrlClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Repository
@EnableCaching
public class OpenExchangeClient implements RatesRepository {

    private static final Logger logger = LoggerFactory.getLogger(OpenExchangeClient.class);

    //TODO check thread safety
    private ObjectMapper mapper = new ObjectMapper();

    @Value("${openexchangerates.app.id}")
    private String appId;

    @Autowired
    UrlClient client;

    // essential URL structure is built using constants
    private final static String BASE_URL = "http://openexchangerates.org/api/";
    private static final String LATEST = "latest.json";
    private static final String HISTORICAL = "historical/%04d-%02d-%02d.json";
    private static final String APP_ID = "?app_id=";

    // https://openexchangerates.org/api/latest.json?app_id=YOUR_APP_APP_ID
    private static final String LIVE_URL = BASE_URL + LATEST + APP_ID;
    private static final String HISTORICAL_URL = BASE_URL + HISTORICAL + APP_ID;

    @Cacheable("liveRates")
    public Map<String, Double> getLive(){
        return getRates(LIVE_URL + appId);
    }

    @Cacheable("historicalRates")
    public Map<String, Double> getHistorical(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        return getRates(String.format(HISTORICAL_URL + appId, year, month, day));
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
