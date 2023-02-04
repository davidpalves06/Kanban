package com.example.Kanban.Authentication;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationUserDetailsServiceTest {

    public static final String TEST_EMAIL = "Teste@gmail.com";
    public static final String TEST_PASSWORD = "encodedPassword";
    AuthenticationUserDetailsService authenticationService;
    @Autowired
    AuthenticationRepository authenticationRepository;
    @BeforeEach
    public void setup(){
        authenticationService = new AuthenticationUserDetailsService(authenticationRepository);
        authenticationRepository.save(new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD));
    }

    @AfterEach
    public void cleanUpMongo() {
        authenticationRepository.deleteAll();
    }


    @Test
    public void loadUserByUsernameShouldSucceed() {
        UserDetails userDetails = authenticationService.loadUserByUsername(TEST_EMAIL);

        assertNotNull(userDetails);
        assertEquals(TEST_EMAIL,userDetails.getUsername());
        assertEquals(TEST_PASSWORD,userDetails.getPassword());
    }

    @Test
    public void loadUserByUsernameShouldThrow() {
        assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.loadUserByUsername("TESTE"));
    }
}