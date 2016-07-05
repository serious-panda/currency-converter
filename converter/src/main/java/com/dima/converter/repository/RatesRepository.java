package com.dima.converter.repository;

import java.time.LocalDate;
import java.util.Map;

public interface RatesRepository {


    Map<String, Double> getLive();


    Map<String, Double> getHistorical(LocalDate date);

}
