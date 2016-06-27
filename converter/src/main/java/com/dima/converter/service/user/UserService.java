package com.dima.converter.service.user;

import com.dima.converter.model.Registration;
import com.dima.converter.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByUsername(String username);

    Collection<User> getAllUsers();

    User create(Registration form);

}