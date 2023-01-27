package com.example.Kanban.UserAccount;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserAccountResponse {
    private String message;
    private List<String> errors;
    private UserAccountDTO accountDTO;
}
