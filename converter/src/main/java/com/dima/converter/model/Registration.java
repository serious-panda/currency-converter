package com.dima.converter.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Past;
import java.util.Date;

public class Registration {
    @Size(min=2, max=30)
    String username;
    @Size(min=6, max=18)
    String password;

    String email;

    @NotNull(message = "Birthday is required.")
    @Past(message = "Invalid date. Must be in the past.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    Date birthday;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
//        try
//        {
//            InternetAddress internetAddress = new InternetAddress(email);
//            internetAddress.validate();
//            return true;
//        }
//        catch(Exception ex)
//        {
//            return false;
//        }
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
