package com.example.Kanban.Authentication.register.validators;

import com.example.Kanban.Authentication.exception.InvalidUserDetailsException;

import static com.example.Kanban.Authentication.register.validators.UserDetailsValidationErrors.*;

public class UserDetailsValidatorImpl implements UserDetailsValidator {

    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){4,18}[a-zA-Z0-9]$";
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";



    public void validateUsername(String username) throws InvalidUserDetailsException {
        if (username == null || !username.matches(USERNAME_PATTERN))
            throw new InvalidUserDetailsException(INVALID_USERNAME.toString());
    }
    public void validatePassword(String password) throws InvalidUserDetailsException {
        if (password == null || !password.matches(PASSWORD_PATTERN))
            throw new InvalidUserDetailsException(INVALID_PASSWORD.toString());
    }
    public void validateEmail(String email) throws InvalidUserDetailsException {
        if (email == null || !email.matches(EMAIL_PATTERN))
            throw new InvalidUserDetailsException(INVALID_EMAIL.toString());
    }
}
