package com.dima.converter.controller;

import com.dima.converter.model.ConversionQuery;
import com.dima.converter.model.CurrentUser;
import com.dima.converter.model.QueryResult;
import com.dima.converter.service.converter.ConversionService;
import com.dima.converter.service.converter.SymbolNotSupportedException;
import com.dima.converter.service.user.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final ConversionService service;

    private final HistoryService historyService;

    //private final static String HOME_VIEW = "classpath:resources/templates/home";
    private final static String HOME_VIEW = "home";

    @Autowired
    public HomeController(ConversionService conversionService, HistoryService historyService){
        this.service = conversionService;
        this.historyService = historyService;
    }

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping("/home")
    public String mainPage(@AuthenticationPrincipal CurrentUser currentUser, ConversionQuery conversionQuery, Model model) {

        model.addAttribute("history", historyService.getUserHistory(currentUser.getUsername()));
        model.addAttribute("currencies", currencies);

        return HOME_VIEW;
    }

    //@PreAuthorize("hasRole('USER')")
    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String query(@AuthenticationPrincipal CurrentUser currentUser,
                        @Valid ConversionQuery conversionQuery,
                        Model model, BindingResult bindingResult) {

        model.addAttribute("history", historyService.getUserHistory(currentUser.getUsername()));
        model.addAttribute("currencies", currencies);

        if (bindingResult.hasErrors()) {
            return HOME_VIEW;
        }
        try {
            double res = conversionQuery.getDate() == null ?
                    service.convert(conversionQuery.getAmount(), conversionQuery.getFrom(), conversionQuery.getTo()) :
                    service.convert(conversionQuery.getAmount(), conversionQuery.getFrom(), conversionQuery.getTo(), conversionQuery.getDate());
            QueryResult queryResult = new QueryResult(conversionQuery,res);

            model.addAttribute("result", queryResult);
            historyService.create(currentUser.getUsername(), queryResult);
        } catch (SymbolNotSupportedException e){
            bindingResult.reject("symbol not supported");
        }
        return HOME_VIEW;
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
