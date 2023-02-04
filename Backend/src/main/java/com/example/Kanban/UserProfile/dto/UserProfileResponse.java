package com.example.Kanban.UserProfile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileResponse {
    private String message;
    private UserProfileDTO accountDTO;
}
