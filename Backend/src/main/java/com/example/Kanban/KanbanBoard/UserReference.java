package com.example.Kanban.KanbanBoard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class UserReference {
    private String userID;
    private String username;

}
