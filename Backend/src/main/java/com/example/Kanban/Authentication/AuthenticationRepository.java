package com.example.Kanban.Authentication;

import com.example.Kanban.UserAccount.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthenticationRepository extends MongoRepository<AuthenticationUserDetails,String> {
        boolean existsByEmail(String email);
        boolean existsByUsername(String username);
        Optional<AuthenticationUserDetails> findByEmail(String email);
}

