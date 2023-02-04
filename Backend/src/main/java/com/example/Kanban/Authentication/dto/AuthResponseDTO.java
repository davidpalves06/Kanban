package com.example.Kanban.Authentication.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuthResponseDTO {
    @JsonProperty(required = true)
    private final String message;
    private String token;

    @JsonCreator
    public AuthResponseDTO(String message) {
        this.message = message;
    }
}
