package com.stockapp.userservice.web.rest.vm;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * View Model for user registration
 */
public class RegisterVM implements Serializable {

    @NotNull
    @Size(min = 3, max = 50)
    private String login;

    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    @NotNull
    @Email
    private String email;

    @Size(max = 200)
    private String fullName;

    @Size(max = 10)
    private String language;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "RegisterVM{" +
                "login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
