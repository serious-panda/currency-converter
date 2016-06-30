package com.dima.converter.service.converter;

public class SymbolNotSupportedException extends Exception {

    public  SymbolNotSupportedException(String symbol){
        super(String.format("Symbol %s is not supported.", symbol));
    }
}
