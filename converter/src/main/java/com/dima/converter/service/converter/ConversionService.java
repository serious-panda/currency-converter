package com.dima.converter.service.converter;

import java.time.LocalDate;

public interface ConversionService {

    double convert(double amount, String fromCurrency, String toCurrency) throws SymbolNotSupportedException, ConversionServiceException;

    double convert(double amount, String fromCurrency, String toCurrency, LocalDate date) throws SymbolNotSupportedException, ConversionServiceException;
}
