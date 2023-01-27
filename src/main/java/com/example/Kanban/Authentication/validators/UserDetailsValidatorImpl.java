package com.example.Kanban.Authentication.validators;

import com.example.Kanban.UserAccount.exception.InvalidUserInfoException;

import static com.example.Kanban.Authentication.validators.UserDetailsValidationErrors.*;

public class UserDetailsValidatorImpl implements UserDetailsValidator {

    private static final String USERNAME_PATTERN =
            "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){4,18}[a-zA-Z0-9]$";
    private static final String EMAIL_PATTERN = "^(.+)@(\\S+)$";
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";



    public void validateUsername(String username) throws InvalidUserInfoException {
        if (username == null || !username.matches(USERNAME_PATTERN))
            throw new InvalidUserInfoException(INVALID_USERNAME.toString());
    }
    public void validatePassword(String password) throws InvalidUserInfoException {
        if (password == null || !password.matches(PASSWORD_PATTERN))
            throw new InvalidUserInfoException(INVALID_PASSWORD.toString());
    }
    public void validateEmail(String email) throws InvalidUserInfoException {
        if (email == null || !email.matches(EMAIL_PATTERN))
            throw new InvalidUserInfoException(INVALID_EMAIL.toString());
    }
}
