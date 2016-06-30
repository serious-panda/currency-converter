package com.dima.converter.service.converter;

import java.util.Date;

public interface ConversionService {

    double convert(double amount, String fromCurrency, String toCurrency) throws SymbolNotSupportedException;

    double convert(double amount, String fromCurrency, String toCurrency, Date date) throws SymbolNotSupportedException;
}
