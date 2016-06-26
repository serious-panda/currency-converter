package com.dima.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CurrencylayerConverter implements ConversionService {

    @Autowired
    private RatesLoader ratesLoader;

    //private ConcurrentMap<String, Double> ratesCache = new ConcurrentHashMap<>();

    @Override
    public double convert(double amount, String base, String quote, Date date) {
        String tmpBase, tmpQuote;
        if (base.compareTo(quote) <= 0) {
            tmpBase = base; tmpQuote = quote;
        } else {
            tmpBase = quote; tmpQuote = base;
        }
        Double rate = ratesLoader.getHistorical(tmpBase, tmpQuote,date);
        return base.equals(tmpBase) ? amount * rate : amount / rate;
    }

    @Override
    public double convert(double amount, String base, String quote) {
        String tmpBase, tmpQuote;
        if (base.compareTo(quote) <= 0) {
            tmpBase = base; tmpQuote = quote;
        } else {
            tmpBase = quote; tmpQuote = base;
        }
        Double rate = ratesLoader.getLive(tmpBase, tmpQuote);
        return base.equals(tmpBase) ? amount * rate : amount / rate;
//        String key = base.compareTo(quote) <= 0 ? base + quote : quote + base;
//        Double rate = ratesLoader.getLive(base, quotes);
//        if (!ratesCache.containsKey(key)){
//            updateCache(base, Arrays.asList(new String[]{quote}));
//        }
//
//        Double rate = ratesCache.get(key);
//        if (rate == null){
//            return -1;
//            // TODO throw conversion exception
//        } else {
//            return key.startsWith(base) ? amount * rate : amount / rate;
//        }
    }

//    private void updateCache(String base, List<String> quotes){
//        Map<String, Double> rates = ratesLoader.getLive(base, quotes);
//        for (Map.Entry<String, Double> entry : rates.entrySet()) {
//            if (base.compareTo(entry.getKey()) <= 0){
//                ratesCache.put(base + entry.getKey(), entry.getValue());
//            } else {
//                ratesCache.put(entry.getKey() + base, 1.0 / entry.getValue());
//            }
//        }
//    }

//    private void ratesUpdate(String bas)
//    private static class Quote {
//        String base;
//        String quote;
//        double rate;
//
//        public Quote(String base, String quote, double rate){
//            this.base = base;
//            this.quote = quote;
//            this.rate = rate;
//        }
//
//        double convert(double amount){
//            return amount * rate;
//        }
//        Quote inverse(){
//            return new Quote(quote,base,1.0/rate);
//        }
//    }
}
