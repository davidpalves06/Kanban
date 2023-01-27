package com.example.Kanban.Authentication.validators;

import com.example.Kanban.UserAccount.exception.InvalidUserInfoException;

public interface UserDetailsValidator {

    void validateUsername(String username) throws InvalidUserInfoException;
    void validateEmail(String email) throws InvalidUserInfoException;
    void validatePassword(String password) throws InvalidUserInfoException;
}
