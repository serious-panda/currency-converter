package com.dima.converter.service;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class RestRatesLoader implements RatesLoader {
    String apiKey;

    double load(String from, String to){
        return 1;
    }

    double load(String from, String to, Date date){
        return 1;
    }

    // essential URL structure is built using constants
    private static final String ACCESS_KEY = "YOUR_ACCESS_KEY";
    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String LIVE_ENDPOINT = "historical";
    private static final String HISTORICAL_ENDPOINT = "live";

    // this object is used for executing requests to the (REST) API
    static CloseableHttpClient httpClient = HttpClients.createDefault();


    /**
     *
     * Notes:
     *
     * A JSON response of the form {"key":"value"} is considered a simple Java JSONObject.
     * To get a simple value from the JSONObject, use: <JSONObject identifier>.get<Type>("key");
     *
     * A JSON response of the form {"key":{"key":"value"}} is considered a complex Java JSONObject.
     * To get a complex value like another JSONObject, use: <JSONObject identifier>.getJSONObject("key")
     *
     * Values can also be JSONArray Objects. JSONArray objects are simple, consisting of multiple JSONObject Objects.
     *
     *
     */
    // sendLiveRequest() function is created to request and retrieve the data
    private void sendLiveRequest(List<String> currencies){
        // The following line initializes the HttpGet Object with the URL in order to send a request
        HttpGet get = new HttpGet(BASE_URL + LIVE_ENDPOINT + "?access_key=" + ACCESS_KEY + "&currencies=" + String.join(",", currencies));

        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            // the following line converts the JSON Response to an equivalent Java Object
            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

            // Parsed JSON Objects are accessed according to the JSON resonse's hierarchy, output strings are built
            Date timeStampDate = new Date((long)(exchangeRates.getLong("timestamp")*1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);
            System.out.println("1 " + exchangeRates.getString("source") + " in GBP : " + exchangeRates.getJSONObject("quotes").getDouble("USDGBP") + " (Date: " + formattedDate + ")");
            System.out.println("\n");
            response.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // sendLiveRequest() function is created to request and retrieve the data
    private void sendHistoricalRequest(Date date, List<String> currencies){
        DateFormat historicalDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // The following line initializes the HttpGet Object with the URL in order to send a request
        HttpGet get = new HttpGet(BASE_URL + HISTORICAL_ENDPOINT + "?access_key=" + ACCESS_KEY + "&currencies=" + String.join(",", currencies) + "&date=" + historicalDateFormat.format(date));

        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();

            // the following line converts the JSON Response to an equivalent Java Object
            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

            // Parsed JSON Objects are accessed according to the JSON resonse's hierarchy, output strings are built
            Date timeStampDate = new Date((long)(exchangeRates.getLong("timestamp")*1000));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
            String formattedDate = dateFormat.format(timeStampDate);
            System.out.println("1 " + exchangeRates.getString("source") + " in GBP : " + exchangeRates.getJSONObject("quotes").getDouble("USDGBP") + " (Date: " + formattedDate + ")");
            System.out.println("\n");
            response.close();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
