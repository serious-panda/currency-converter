package com.dima.converter.controller;

import com.dima.converter.model.Registration;
import com.dima.converter.model.Role;
import com.dima.converter.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService){
        // convenience for demo only.
        // no validation here, only username and pw are important
        this.userService = userService;
        Registration r = new Registration();
        r.setUsername("a");
        r.setPassword("a");
        r.setEmail("anything");
        r.setBirthday(LocalDate.now());
        r.setRole(Role.ADMIN);
        this.userService.create(r);
    }

    @RequestMapping(value="/register")
    public String registrationForm(Registration registration) {
        return "registration";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String registrationSubmit(@Valid Registration registration, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            userService.create(registration);
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("user.exists", "User with this name and/or email already exists");
            return "registration";
        }
        return "redirect:/home";
    }
}
