package com.dima.converter.model;

public class QueryResult {

    private final ConversionQuery query;
    private final double result;

    public QueryResult(ConversionQuery query, double result) {
        this.query = query;
        this.result = result;
    }

    public ConversionQuery getQuery() {
        return query;
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "query=" + query +
                ", result=" + result +
                '}';
    }
}
