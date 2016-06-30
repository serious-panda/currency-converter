package com.dima.converter.service.converter;

import com.dima.converter.repository.RatesRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Configuration
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class TransitiveCurrencyConverterTest {

    @Mock
    RatesRepository ratesRepository;

    TransitiveCurrencyConverter converter;

    @Before
    public void setUp() throws Exception {
        converter = new TransitiveCurrencyConverter(ratesRepository);
    }

    @Test
    public void convert() throws Exception {
        Map<String, Double> rates = createRatesMap();

        double amount = 12;
        double res = amount * (1/rates.get("aaa")) * rates.get("bbb");

        BigDecimal bd = new BigDecimal(Double.valueOf(res));
        res = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

        when(ratesRepository.getLive()).thenReturn(rates);
        double val = converter.convert(amount, "aaa", "bbb");
        assertThat(val).isEqualTo(res);
    }

    @Test(expected=SymbolNotSupportedException.class)
    public void convertLiveUnknown() throws Exception {
        Map<String, Double> rates = new HashMap<>();
        double amount = 12;

        when(ratesRepository.getLive()).thenReturn(rates);
        double val = converter.convert(amount, "aaa", "bbb");

    }

    @Test
    public void convertHistorical() throws Exception {
        Map<String, Double> rates = createRatesMap();
        double amount = 12;
        double res = amount * (1/rates.get("aaa")) * rates.get("bbb");

        BigDecimal bd = new BigDecimal(Double.valueOf(res));
        res = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        Date now = new Date(); // it doesn't matter at a level of repository if the date is not a historical.
        when(ratesRepository.getHistorical(now)).thenReturn(rates);
        double val = converter.convert(amount, "aaa", "bbb",now);
        assertThat(val).isEqualTo(res);

    }


    private Map<String, Double> createRatesMap(){
        Map<String, Double> rates = new HashMap<>();
        double usdToAaa = 2.3;
        double usdToBbb = 0.45;

        rates.put("aaa", usdToAaa);
        rates.put("bbb", usdToBbb);
        return rates;
    }
}