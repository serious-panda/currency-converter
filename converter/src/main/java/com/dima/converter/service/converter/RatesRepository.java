package com.dima.converter.service.converter;

import java.util.Date;
import java.util.Map;

public interface RatesRepository {

    Map<String, Double> getLive();

    Map<String, Double> getHistorical(Date date);

}
