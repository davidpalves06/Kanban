package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.dto.CreateKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.KanbanBoardResponse;
import com.example.Kanban.UserProfile.UserProfile;
import com.example.Kanban.UserProfile.UserProfileController;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KanbanBoardService {
    public static final String BOARD_CREATED_SUCCESSFULLY = "Board created successfully!";
    public static final String FAIL_TO_CREATE_BOARD = "Failed to create board!";
    private final UserProfileController userProfileController;
    private final KanbanBoardRepository kanbanBoardRepository;

    public ResponseEntity<KanbanBoardResponse> createKanbanBoard(CreateKanbanBoardDTO createKanbanBoardDTO) {
        String userID = createKanbanBoardDTO.getUserID();
        String boardName = createKanbanBoardDTO.getBoardName();

        Optional<UserProfile> userProfileOptional = getProfileResponse(userID);

        return userProfileOptional
                .map(userProfile -> createKanbanBoardAndReturnResponse(userID, boardName, userProfile))
                .orElseGet(() -> new ResponseEntity<>(new KanbanBoardResponse(FAIL_TO_CREATE_BOARD), HttpStatus.CONFLICT));
    }

    private ResponseEntity<KanbanBoardResponse> createKanbanBoardAndReturnResponse(String userID, String boardName, UserProfile userProfile) {
        KanbanBoard kanbanBoard = createKanbanBoard(userID, boardName, userProfile);
        KanbanBoard insertedKanbanBoard = kanbanBoardRepository.insert(kanbanBoard);
        KanbanBoardResponse kanbanBoardResponse = new KanbanBoardResponse(BOARD_CREATED_SUCCESSFULLY);
        kanbanBoardResponse.setBoard(insertedKanbanBoard);
        return new ResponseEntity<>(kanbanBoardResponse, HttpStatus.OK);
    }

    private KanbanBoard createKanbanBoard(String userID, String boardName, UserProfile userProfile) {
        KanbanBoard kanbanBoard = new KanbanBoard(boardName, userID);
        UserReference userReference = new UserReference(userID, userProfile.getUsername());
        kanbanBoard.addUser(userReference);
        return kanbanBoard;
    }

    private Optional<UserProfile> getProfileResponse(String userID) {
        ResponseEntity<UserProfile> getUserProfileResponse = userProfileController.getUserProfile(userID);
        if (getUserProfileResponse.getStatusCode().is2xxSuccessful()) {
            return Optional.of(getUserProfileResponse.getBody());
        }
        else return Optional.empty();
    }
}
