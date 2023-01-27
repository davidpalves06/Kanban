package com.example.Kanban.UserAccount;

import lombok.Data;

@Data
public class UserAccountDTO {
    private String username;
    private String email;
    private String password;
    private String photo;
    private String token;
}
