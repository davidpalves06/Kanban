package com.example.Kanban.KanbanBoard.dto;

import com.example.Kanban.KanbanBoard.cards.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProgressDTO {
    private ProgressStatus progressStatus;
}
