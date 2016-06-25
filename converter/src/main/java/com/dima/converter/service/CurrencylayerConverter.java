package com.dima.converter.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CurrencylayerConverter implements ConversionService {

//    private String apiKey;
//
//    public CurrencylayerConverter(String apiKey){
//
//    }

    @Override
    public double convert(double amount, String fromCurrency, String toCurrency, Date date) {
        return 0;
    }

    @Cacheable
    @Override
    public double convert(double amount, String fromCurrency, String toCurrency) {
        return amount;
    }
}
