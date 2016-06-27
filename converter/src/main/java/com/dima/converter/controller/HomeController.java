package com.dima.converter.controller;

import com.dima.converter.model.ConversionQuery;
import com.dima.converter.model.QueryResult;
import com.dima.converter.service.converter.ConversionService;
import com.dima.converter.service.converter.QueryHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController implements ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private Map<String, String> currencies = new HashMap<>();

    @Autowired
    private ConversionService service;

    @Autowired
    private QueryHistory queryHistory;

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping("/home")
    public String mainPage(ConversionQuery conversionQuery, Model model) {

        model.addAttribute("history", getHistory());
        model.addAttribute("currencies", currencies);
        return "home";
    }

    //@PreAuthorize("hasRole('USER')")
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

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource("classpath:currencies.csv");

        //Files.newBufferedReader(resource.getFile() /*Paths.get("currencies.csv")*/)
        try(BufferedReader bufferReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            currencies = bufferReader.lines().map(l -> l.split(",")).collect(Collectors.toMap(x->x[0], x->x[1]));
        } catch (IOException e) {
            logger.error("Failed to load list of currencies from resources.");
        }

    }
}
