package com.example.Kanban.Authentication.register.validators;

import com.example.Kanban.Authentication.exception.InvalidUserDetailsException;

public interface UserDetailsValidator {

    void validateUsername(String username) throws InvalidUserDetailsException;
    void validateEmail(String email) throws InvalidUserDetailsException;
    void validatePassword(String password) throws InvalidUserDetailsException;
}
