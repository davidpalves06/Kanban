package com.example.Kanban.Authentication;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthenticationRepository extends MongoRepository<AuthenticationUserDetails,String> {
        boolean existsByEmail(String email);
        Optional<AuthenticationUserDetails> findByEmail(String email);
}

