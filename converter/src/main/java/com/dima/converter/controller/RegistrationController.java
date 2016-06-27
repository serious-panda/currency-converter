package com.dima.converter.controller;

import com.dima.converter.model.Registration;
import com.dima.converter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @RequestMapping(value="/register", method = RequestMethod.GET)
    public String registrationForm(Registration registration) {
        return "registration";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String greetingSubmit(@Valid Registration registration, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userService.create(registration);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("email.exists", "Email already exists");
            return "registration";
        }
        return "redirect:/home";
    }
}
