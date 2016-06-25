package com.dima.converter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class CachingRatesRepository implements RatesRepository {

    @Autowired
    private RatesLoader loader;

    @Override
    public double get(String from, String to) {
        return 0;
    }

    @Override
    public double get(String from, String to, Date date) {
        return 0;
    }
}
