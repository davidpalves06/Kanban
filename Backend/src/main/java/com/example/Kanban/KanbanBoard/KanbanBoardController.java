package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.dto.AddCardKanbanBoardDTO;
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

    @PostMapping("/{boardID}/users")
    public ResponseEntity<KanbanBoardResponse> addUserKanbanBoard(@PathVariable String boardID, AddUserKanbanBoardDTO addUserKanbanBoardDTO) {
        return boardService.addUserToBoard(addUserKanbanBoardDTO,boardID);
    }

    @DeleteMapping("/{boardID}/users/{userID}")
    public ResponseEntity<KanbanBoardResponse> removeUserKanbanBoard(@PathVariable String boardID, @PathVariable String userID) {
        return boardService.removeUserFromBoard(boardID,userID);
    }

    @PostMapping("/{boardID}/cards")
    public ResponseEntity<KanbanBoardResponse> addCardKanbanBoard(@PathVariable String boardID, AddCardKanbanBoardDTO addCardKanbanBoard) {
        return boardService.addCardToBoard(addCardKanbanBoard,boardID);
    }

    @DeleteMapping("/{boardID}/cards/{cardID}")
    public ResponseEntity<KanbanBoardResponse> addUserKanbanBoard(@PathVariable String boardID, @PathVariable String cardID) {
        return boardService.removeCardFromBoard(boardID,cardID);
    }
}
