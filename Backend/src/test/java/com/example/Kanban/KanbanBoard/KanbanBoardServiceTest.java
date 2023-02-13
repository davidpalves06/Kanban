package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.cards.KanbanCard;
import com.example.Kanban.KanbanBoard.cards.ProgressStatus;
import com.example.Kanban.KanbanBoard.dto.AddCardKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.AddUserKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.CreateKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.KanbanBoardResponse;
import com.example.Kanban.UserProfile.UserProfile;
import com.example.Kanban.UserProfile.UserProfileController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KanbanBoardServiceTest {

    public static final String TEST_USERNAME = "TEST_USERNAME";
    public static final String USER_ID = "USER_ID";
    public static final String TEST_BOARD_NAME = "TEST_BOARD_NAME";
    public static final String BOARD_ID = "BOARD_ID";
    public static final String TEST_STORY = "TEST_STORY";
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
        assertEquals(1,response.getBody().getBoard().getParticipants().size());
    }

    @Test
    public void createKanbanBoardShouldFail() {
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.createKanbanBoard(new CreateKanbanBoardDTO(USER_ID, TEST_BOARD_NAME));

        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals(KanbanBoardService.FAIL_TO_CREATE_BOARD,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void addUserToBoardShouldSucceed() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        when(userProfileController.getUserProfile("2")).thenReturn(new ResponseEntity<>(userProfile, HttpStatus.OK));
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.of(kanbanBoard));
        when(kanbanBoardRepository.save(any(KanbanBoard.class))).thenAnswer((t) -> t.getArguments()[0]);

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addUserToBoard(new AddUserKanbanBoardDTO("2"), BOARD_ID);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(KanbanBoardService.USER_ADDED_SUCCESSFULLY,response.getBody().getMessage());
        assertTrue(response.getBody().getBoard().getParticipants().containsKey("2"));
        assertEquals(1,response.getBody().getBoard().getParticipants().size());
    }

    @Test
    public void addUserToBoardShouldFailUserAlreadyAdded() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        kanbanBoard.addUser(USER_ID,TEST_USERNAME);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(userProfile, HttpStatus.OK));
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.of(kanbanBoard));

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addUserToBoard(new AddUserKanbanBoardDTO(USER_ID), BOARD_ID);

        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertEquals(KanbanBoardService.USER_ALREADY_ADDED,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void addUserToBoardShouldFailUserNotFound() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addUserToBoard(new AddUserKanbanBoardDTO(USER_ID), BOARD_ID);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void addUserToBoardShouldFailBoardNotFound() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(userProfile, HttpStatus.OK));
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.empty());
        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addUserToBoard(new AddUserKanbanBoardDTO(USER_ID), BOARD_ID);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void removeUserFromBoardShouldSucceed() {
        UserProfile userProfile = new UserProfile(TEST_USERNAME);
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        kanbanBoard.addUser(USER_ID,TEST_USERNAME);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(userProfile, HttpStatus.OK));
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.of(kanbanBoard));
        when(kanbanBoardRepository.save(any(KanbanBoard.class))).thenAnswer((t) -> t.getArguments()[0]);

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.removeUserFromBoard(BOARD_ID,USER_ID);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(KanbanBoardService.USER_REMOVED_SUCCESSFULLY,response.getBody().getMessage());
        assertFalse(response.getBody().getBoard().getParticipants().containsKey(USER_ID));
        assertEquals(0,response.getBody().getBoard().getParticipants().size());
    }

    @Test
    public void removeUserFromBoardShouldFail() {
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        kanbanBoard.addUser(USER_ID,TEST_USERNAME);
        when(userProfileController.getUserProfile(USER_ID)).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.removeUserFromBoard(BOARD_ID,USER_ID);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void addCardToBoardShouldSucceed() {
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.of(kanbanBoard));
        when(kanbanBoardRepository.save(any(KanbanBoard.class))).thenAnswer((t) -> t.getArguments()[0]);

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addCardToBoard(new AddCardKanbanBoardDTO(TEST_STORY), BOARD_ID);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(KanbanBoardService.CARD_ADDED_SUCCESSFULLY,response.getBody().getMessage());
        assertEquals(1,response.getBody().getBoard().getBoardCards().size());
        assertEquals(TEST_STORY,response.getBody().getBoard().getBoardCards().get(0).getUserStory());
    }

    @Test
    public void addCardToBoardShouldFailBoardNotFound() {
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.empty());

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.addCardToBoard(new AddCardKanbanBoardDTO(TEST_STORY), BOARD_ID);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }

    @Test
    public void removeCardFromBoardShouldSucceed() {
        KanbanBoard kanbanBoard = new KanbanBoard(TEST_BOARD_NAME,USER_ID);
        KanbanCard kanbanCard = new KanbanCard(TEST_STORY, ProgressStatus.IN_PROGRESS);
        kanbanBoard.addCard(kanbanCard);

        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.of(kanbanBoard));
        when(kanbanBoardRepository.save(any(KanbanBoard.class))).thenAnswer((t) -> t.getArguments()[0]);

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.removeCardFromBoard(BOARD_ID,kanbanCard.getId());

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(KanbanBoardService.CARD_REMOVED_SUCCESSFULLY,response.getBody().getMessage());
        assertEquals(0,response.getBody().getBoard().getBoardCards().size());
    }

    @Test
    public void removeCardFromBoardShouldFail() {
        when(kanbanBoardRepository.findById(BOARD_ID)).thenReturn(Optional.empty());

        ResponseEntity<KanbanBoardResponse> response = kanbanBoardService.removeCardFromBoard(BOARD_ID,"1");

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,response.getBody().getMessage());
        assertNull(response.getBody().getBoard());
    }
}