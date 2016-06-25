package com.dima.converter.service;

import java.util.Date;

public interface RatesRepository {
    double get(String from, String to);
    double get(String from, String to, Date date);
}
