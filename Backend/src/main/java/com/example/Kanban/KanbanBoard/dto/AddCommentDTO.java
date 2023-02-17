package com.example.Kanban.KanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentDTO {
    private String username;
    private String comment;
}
