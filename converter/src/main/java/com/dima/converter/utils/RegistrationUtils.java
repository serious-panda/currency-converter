package com.dima.converter.utils;

import com.dima.converter.model.Registration;
import com.dima.converter.model.jpa.Role;
import com.dima.converter.service.user.UserService;

import java.time.LocalDate;

/**
 * Utility class for pre populating in-memory JPA persistence.
 * Creates user with name 'a' and pw 'a' for quick login.
 */
public class RegistrationUtils {

    public static void createDefaultUser(UserService service){
        // no validation here, only username and pw are important
        Registration r = new Registration();
        r.setUsername("a");
        r.setPassword("a");
        r.setEmail("dontcare");
        r.setBirthday(LocalDate.now());
        r.setRole(Role.ADMIN);
        service.create(r);
    }
}
