package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.dto.AddUserKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.CreateKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.KanbanBoardResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards")
@AllArgsConstructor
public class KanbanBoardController {

    private final KanbanBoardService boardService;


    @PostMapping("")
    public ResponseEntity<KanbanBoardResponse> createKanbanBoard(CreateKanbanBoardDTO createKanbanBoardDTO) {
        return boardService.createKanbanBoard(createKanbanBoardDTO);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<KanbanBoardResponse> addUserKanbanBoard(@PathVariable String id, AddUserKanbanBoardDTO addUserKanbanBoardDTO) {
        return boardService.addUserToBoard(addUserKanbanBoardDTO,id);
    }

    @DeleteMapping("/{boardID}/users/{userID}")
    public ResponseEntity<KanbanBoardResponse> addUserKanbanBoard(@PathVariable String boardID, @PathVariable String userID) {
        return boardService.removeUserFromBoard(boardID,userID);
    }
}
