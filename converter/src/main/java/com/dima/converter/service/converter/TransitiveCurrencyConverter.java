package com.dima.converter.service.converter;

import com.dima.converter.repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransitiveCurrencyConverter implements ConversionService {
    private final static int CONVERSION_PRECISION = 5;

    private final RatesRepository ratesRepository;

    @Autowired
    public TransitiveCurrencyConverter(RatesRepository ratesRepository){
        this.ratesRepository = ratesRepository;
    }

    @Override
    public double convert(double amount, String base, String quote, LocalDate date) throws SymbolNotSupportedException, ConversionServiceException {
        Map<String, Double> rates = ratesRepository.getHistorical(date);
        return convert(amount,base,quote,rates);
    }

    @Override
    public double convert(double amount, String base, String quote) throws SymbolNotSupportedException, ConversionServiceException {
        Map<String, Double> rates = ratesRepository.getLive();
        return convert(amount,base,quote,rates);
    }

    private double convert(double amount, String base, String quote, Map<String, Double> rates) throws SymbolNotSupportedException, ConversionServiceException {
        if (!rates.containsKey(base)){
            throw new SymbolNotSupportedException(base);
        }
        if (!rates.containsKey(quote)){
            throw new SymbolNotSupportedException(quote);
        }
        if (rates.isEmpty()) {
            throw new ConversionServiceException("Rates not available.");
        }
        // convert base to USD
        BigDecimal bdAmount = new BigDecimal(Double.toString(amount));
        BigDecimal bdToUsdRate = new BigDecimal(Double.toString(rates.get(base)));
        BigDecimal bdInUsd = bdAmount.divide(bdToUsdRate, CONVERSION_PRECISION, RoundingMode.HALF_UP );
        BigDecimal bdResult = bdInUsd.multiply(new BigDecimal(Double.toString(rates.get(quote))));
        return bdResult.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
