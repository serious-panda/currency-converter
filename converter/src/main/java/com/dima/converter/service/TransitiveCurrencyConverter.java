package com.dima.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class TransitiveCurrencyConverter implements ConversionService {
    private final static int CONVERSION_PRECISION = 5;
    @Autowired
    private RatesRepository ratesRepository;

    @Override
    public double convert(double amount, String base, String quote, Date date) {
        Map<String, Double> rates = ratesRepository.getHistorical(date);
        return convert(amount,base,quote,rates);
    }

    @Override
    public double convert(double amount, String base, String quote) {
        Map<String, Double> rates = ratesRepository.getLive();
        return convert(amount,base,quote,rates);
    }

    private double convert(double amount, String base, String quote, Map<String, Double> rates){
        if (!rates.containsKey(base) || !rates.containsKey(quote)){
            throw new IllegalArgumentException("Unknown symbols");
        }
        // convert base to USD
        BigDecimal bdAmount = new BigDecimal(Double.toString(amount));
        BigDecimal bdToUsdRate = new BigDecimal(Double.toString(rates.get(base)));
        BigDecimal bdInUsd = bdAmount.divide(bdToUsdRate, CONVERSION_PRECISION, RoundingMode.HALF_UP );
        BigDecimal bdResult = bdInUsd.multiply(new BigDecimal(Double.toString(rates.get(quote))));
        return bdResult.setScale(2, RoundingMode.HALF_UP).doubleValue();
        //double usd = amount / rates.get(base);
        //return usd * rates.get(quote);
    }
}
