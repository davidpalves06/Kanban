package com.example.Kanban.UserProfile;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProfileRepository extends MongoRepository<UserProfile,String> {
}
