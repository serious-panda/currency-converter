package com.dima.converter.controller;

import com.dima.converter.model.QueryResult;
import com.dima.converter.model.ConversionQuery;
import com.dima.converter.service.ConversionService;
import com.dima.converter.service.QueryHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    Map<String, String> currencies = new HashMap<>();

    @Autowired
    private ConversionService service;

    @Autowired
    QueryHistory queryHistory;

    HomeController(){
        currencies.put("AAA", "dfdsfdsfd");
        currencies.put("BBB", "4353456345");
        currencies.put("CCC", "sdfsd 4334 34 dfsd");
    }

    @RequestMapping("/home")
    public String mainPage(ConversionQuery conversionQuery, Model model) {

        model.addAttribute("history", getHistory());
        model.addAttribute("currencies", currencies);
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String query(@Valid ConversionQuery conversionQuery, Model model, BindingResult bindingResult) {

        model.addAttribute("history", getHistory());
        model.addAttribute("currencies", currencies);

        if (bindingResult.hasErrors()) {
            return "home";
        }

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        double res = service.convert(conversionQuery.getAmount(), conversionQuery.getFrom(), conversionQuery.getTo());
        QueryResult queryResult = new QueryResult(conversionQuery,res);
        model.addAttribute("result", queryResult);
        QueryHistory.Entry entry = new QueryHistory.Entry(new Date(),queryResult);
        queryHistory.add(name, entry);
        return "home";
    }


    private QueryHistory.Entry[] getHistory(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return queryHistory.get(auth.getName());
    }
}
