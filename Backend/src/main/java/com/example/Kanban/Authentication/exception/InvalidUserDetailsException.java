package com.example.Kanban.Authentication.exception;

public class InvalidUserDetailsException extends AuthenticationException{
    public InvalidUserDetailsException() {
    }

    public InvalidUserDetailsException(String message) {
        super(message);
    }
}
