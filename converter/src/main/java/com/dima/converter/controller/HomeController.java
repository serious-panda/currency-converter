package com.dima.converter.controller;

import com.dima.converter.model.ConversionQuery;
import com.dima.converter.model.QueryResult;
import com.dima.converter.service.converter.ConversionService;
import com.dima.converter.service.user.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController implements ResourceLoaderAware {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private List<CurrencyModel> currencies = new ArrayList<>();

    @Autowired
    private ConversionService service;

    @Autowired
    private HistoryService historyService;

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping("/home")
    public String mainPage(ConversionQuery conversionQuery, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        model.addAttribute("history", historyService.getUserHistory(auth.getName()));
        model.addAttribute("currencies", currencies);

        return "home";
    }

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String query(@Valid ConversionQuery conversionQuery, Model model, BindingResult bindingResult) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("history", historyService.getUserHistory(username));
        model.addAttribute("currencies", currencies);

        if (bindingResult.hasErrors()) {
            return "home";
        }

        double res = conversionQuery.getDate() == null?
                service.convert(conversionQuery.getAmount(), conversionQuery.getFrom(), conversionQuery.getTo()) :
                service.convert(conversionQuery.getAmount(), conversionQuery.getFrom(), conversionQuery.getTo(), conversionQuery.getDate());

        QueryResult queryResult = new QueryResult(conversionQuery,res);

        model.addAttribute("result", queryResult);
        historyService.create(username, queryResult);
        return "home";
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        Resource resource = resourceLoader.getResource("classpath:currencies.csv");

        try(BufferedReader bufferReader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            currencies = bufferReader.lines()
                                        .map(l -> l.split(","))
                                        .map(x->new CurrencyModel(x[0],x[1]))
                                        .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed to load list of currencies from resources.");
        }

    }

    public static class CurrencyModel {
        private final String name;
        private final String title;
        public  CurrencyModel(String name, String title){
            this.name = name;
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }
    }
}
