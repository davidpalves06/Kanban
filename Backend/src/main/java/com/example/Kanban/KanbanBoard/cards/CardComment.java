package com.example.Kanban.KanbanBoard.cards;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardComment {
    private String username;
    private String commentText;
}
