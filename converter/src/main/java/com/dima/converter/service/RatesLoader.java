package com.dima.converter.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RatesLoader {

    Map<String, Double> getLive(String base, List<String> quotes);

    // TODO make optional
    Double getLive(String base, String quote);

    Map<String, Double> getHistorical(String base, List<String> quotes, Date date);

    //TODO make optional
    Double getHistorical(String base, String quote, Date date);

}
