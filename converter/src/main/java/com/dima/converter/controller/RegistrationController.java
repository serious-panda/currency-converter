package com.dima.converter.controller;

import com.dima.converter.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String registrationForm(Registration registration) {
        return "registration";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String greetingSubmit(@Valid Registration registration, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        jdbcTemplate.execute("insert into users (username, password, enabled) values ('" + registration.getUsername() + "', '" + registration.getPassword() + "', true);");
        jdbcTemplate.execute("insert into authorities (username, authority) values ('" + registration.getUsername() + "', 'ROLE_ADMIN');");

        return "redirect:/home";

    }
}
