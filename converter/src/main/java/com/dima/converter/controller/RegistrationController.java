package com.dima.converter.controller;

import com.dima.converter.model.Registration;
import com.dima.converter.service.user.UserService;
import com.dima.converter.utils.RegistrationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    private final UserService userService;

    private final static String REG_VIEW = "registration";

    @Autowired
    public RegistrationController(UserService userService){
        // convenience, for demo only. should not be in real application
        // no validation here, only username and pw are important
        this.userService = userService;
        RegistrationUtils.createDefaultUser(this.userService);
    }

    @RequestMapping(value="/register")
    public String registrationForm(Registration registration) {
        return REG_VIEW;
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String registrationSubmit(@Valid Registration registration, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return REG_VIEW;
        }
        try {
            userService.create(registration);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("user.exists", "User with this name and/or email already exists");
            return REG_VIEW;
        }
        return "redirect:/home";
    }
}
