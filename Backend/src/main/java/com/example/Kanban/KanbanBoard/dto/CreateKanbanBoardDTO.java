package com.example.Kanban.KanbanBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateKanbanBoardDTO {
    private String userID;
    private String boardName;
}
