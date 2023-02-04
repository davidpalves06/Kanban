package com.example.Kanban.Authentication.validators;

import com.example.Kanban.Authentication.register.validators.UserDetailsValidator;
import com.example.Kanban.Authentication.register.validators.UserDetailsValidatorImpl;
import com.example.Kanban.Authentication.exception.InvalidUserDetailsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    UserDetailsValidator validator = new UserDetailsValidatorImpl();
    @Test
    public void validUsernameTest() {
        String[] validUsernames = {"David123","david.engineer","test_valid-1"};
        for (String validUsername : validUsernames) {
            assertDoesNotThrow(() -> {
                validator.validateUsername(validUsername);
            });
        }
    }

    @Test
    public void invalidUsernameTest() {
        String[] invalidUsernames = {"_David123","Test--23","small","THIS-IS-A-TEST-FOR-LENGTH","end.with_special-"};
        for (String invalidUsername : invalidUsernames) {
            assertThrows(InvalidUserDetailsException.class,() -> {
                validator.validateUsername(invalidUsername);
            });
        }
    }

    @Test
    public void validPasswordTest() {
        String[] validPasswords = {"testPassword1","LebronJAMES6","MyDoGiS7"};
        for (String validPassword : validPasswords) {
            assertDoesNotThrow(() -> {
                validator.validatePassword(validPassword);
            });
        }
    }

    @Test
    public void invalidPasswordTest() {
        String[] invalidPasswords = {"NOLOWERCASE7","NOdigits","nouppercase8","Less8","moreThan20CharactersShouldFail"};
        for (String invalidPassword : invalidPasswords) {
            assertThrows(InvalidUserDetailsException.class,() -> {
                validator.validatePassword(invalidPassword);
            });
        }
    }

    @Test
    public void validEmailTest() {
        String[] validEmails = {"user@domain.com","user.surname@domain.com","user@domain.com.in"};
        for (String validEmail : validEmails) {
            assertDoesNotThrow(() -> {
                validator.validateEmail(validEmail);
            });
        }
    }

    @Test
    public void invalidEmailTest() {
        String[] invalidEmails = {"user.domain.com","user.surname#domain.com","username"};
        for (String invalidEmail : invalidEmails) {
            assertThrows(InvalidUserDetailsException.class,() -> {
                validator.validateEmail(invalidEmail);
            });
        }
    }
}
