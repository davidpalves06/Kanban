package com.example.Kanban.UserAccount;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount,String> {
}
