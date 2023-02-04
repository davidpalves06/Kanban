package com.example.Kanban.UserProfile;

import com.example.Kanban.UserProfile.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public ResponseEntity<String> createUserProfile(UserProfileDTO userProfileDTO) {
        return userProfileService.createUserProfile(userProfileDTO);
    }
}
