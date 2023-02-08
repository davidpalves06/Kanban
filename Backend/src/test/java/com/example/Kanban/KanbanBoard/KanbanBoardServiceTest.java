package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.dto.CreateKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.KanbanBoardResponse;
import com.example.Kanban.UserProfile.UserProfile;
import com.example.Kanban.UserProfile.UserProfileController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KanbanBoardServiceTest {

    public static final String TEST_USERNAME = "TEST_USERNAME";
    public static final String USER_ID = "USER_ID";
    public static final String TEST_BOARD_NAME = "TEST_BOARD_NAME";
    @Mock
    private UserProfileController userProfileController;
    @Mock
    private KanbanBoardRepository kanbanBoardRepository;
    @InjectMocks
    private KanbanBoardService kanbanBoardService;

    @Test
    public void createKanbanBoardShouldSucceed() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(userProfile, HttpStatus.OK));
        when(kanbanBoardRepository.insert(any(KanbanBoard.class))).thenAnswer((t) -> t.getArguments()[0]);

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.createKanbanBoard(new CreateKanbanBoardDTO(USER_ID, TEST_BOARD_NAME));

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(KanbanBoardService.BOARD_CREATED_SUCCESSFULLY,response.getBody().getMessage());
        assertEquals(USER_ID,response.getBody().getBoard().getOwnerID());
        assertEquals(TEST_BOARD_NAME,response.getBody().getBoard().getName());
        assertEquals(1,response.getBody().getBoard().getParticipantsList().size());
    }

    @Test
    public void createKanbanBoardShouldFail() {
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.createKanbanBoard(new CreateKanbanBoardDTO(USER_ID, TEST_BOARD_NAME));

        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals(KanbanBoardService.FAIL_TO_CREATE_BOARD,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }
}