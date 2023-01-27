package com.example.Kanban.Authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationUserDetailsServiceTest {

    AuthenticationUserDetailsService authenticationService;
    AuthenticationRepository authenticationRepository = mock(AuthenticationRepository.class);
    @BeforeEach
    public void setup(){
        authenticationService = new AuthenticationUserDetailsService(authenticationRepository);
    }


    @Test
    public void loadUserByUsernameShouldSucceed() {
        when(authenticationRepository.findByEmail("Teste@gmail.com"))
                .thenReturn(Optional.of(new AuthenticationUserDetails("Teste@gmail.com", "encodedPassword")));

        UserDetails userDetails = authenticationService.loadUserByUsername("Teste@gmail.com");

        assertNotNull(userDetails);
    }

    @Test
    public void loadUserByUsernameShouldThrow() {
        when(authenticationRepository.findByEmail("Teste@gmail.com"))
                .thenReturn(Optional.empty());


        assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.loadUserByUsername("Teste@gmail.com"));
    }
}