package com.example.Kanban.Authentication.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private final String message;
    private String token;
}
