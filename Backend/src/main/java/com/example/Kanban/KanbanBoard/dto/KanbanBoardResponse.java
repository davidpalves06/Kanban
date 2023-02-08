package com.example.Kanban.KanbanBoard.dto;

import com.example.Kanban.KanbanBoard.KanbanBoard;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KanbanBoardResponse {
    @JsonProperty(required = true)
    private final String message;
    private KanbanBoard board;

    @JsonCreator
    public KanbanBoardResponse(String message) {
        this.message = message;
        this.board = null;
    }
}
