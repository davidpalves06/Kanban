package com.example.Kanban.Authentication;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationRepositoryTest {

    public static final String TEST_EMAIL = "teste@gmail.com";
    public static final String TEST_PASSWORD = "password123";
    @Autowired
    AuthenticationRepository authenticationRepository;

    @AfterEach
    public void cleanUpMongo() {
        authenticationRepository.deleteAll();
    }

    @Test
    public void saveShouldSucceed() {
        AuthenticationUserDetails userDetails = authenticationRepository.insert(new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD));

        assertNotNull(userDetails);
        assertTrue(userDetails.getId().length() > 0);

    }

    @Test
    public void saveShouldThrow() {
        authenticationRepository.insert(new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD));

        assertThrows(DuplicateKeyException.class,() ->
                authenticationRepository.insert(new AuthenticationUserDetails(TEST_EMAIL, TEST_PASSWORD)));
    }


    @Test
    public void existsByEmailShouldReturnTrue() {
        AuthenticationUserDetails userDetails = authenticationRepository.insert(new AuthenticationUserDetails(TEST_EMAIL,TEST_PASSWORD));
        assertTrue(authenticationRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    public void existsByEmailShouldReturnFalse() {
        assertFalse(authenticationRepository.existsByEmail(TEST_EMAIL));
    }

    @Test
    public void findByEmailShouldReturnOptional() {
        AuthenticationUserDetails userDetails = authenticationRepository.insert(new AuthenticationUserDetails(TEST_EMAIL,TEST_PASSWORD));
        Optional<AuthenticationUserDetails> userDetailsByEmail = authenticationRepository.findByEmail(TEST_EMAIL);

        assertTrue(userDetailsByEmail.isPresent());
        assertEquals(userDetails,userDetailsByEmail.get());
    }

    @Test
    public void findByEmailShouldReturnEmpty() {
        assertTrue(authenticationRepository.findByEmail(TEST_EMAIL).isEmpty());
    }
}