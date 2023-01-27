package com.example.Kanban.Authentication;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationUserDetailsService implements UserDetailsService {

    private AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<AuthenticationUserDetails> userDetailsOptional = authenticationRepository.findByEmail(email);
        if (userDetailsOptional.isEmpty()) throw new UsernameNotFoundException("User was not found.");
        return userDetailsOptional.get();
    }
}
