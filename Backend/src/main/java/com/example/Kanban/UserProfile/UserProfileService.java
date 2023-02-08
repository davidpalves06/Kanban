package com.example.Kanban.UserProfile;

import com.example.Kanban.UserProfile.dto.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public ResponseEntity<String> createUserProfile(UserProfileDTO userProfileDTO) {
        UserProfile userProfile = new UserProfile(userProfileDTO.getUsername());
        userProfile.setId(userProfileDTO.getId());

        try {
            userProfileRepository.insert(userProfile);
            return new ResponseEntity<>("Profile created successfully!", HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>("Error creating the profile!",HttpStatus.CONFLICT);
        }

    }

    public ResponseEntity<UserProfile> getUserProfile(String id) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(id);
        return optionalUserProfile
                .map(userProfile -> new ResponseEntity<>(userProfile, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
