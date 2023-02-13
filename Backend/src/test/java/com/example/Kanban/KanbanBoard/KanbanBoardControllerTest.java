package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.dto.AddCardKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.AddUserKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.CreateKanbanBoardDTO;
import com.example.Kanban.KanbanBoard.dto.KanbanBoardResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = KanbanBoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class KanbanBoardControllerTest {
    public static final String USER_ID = "USER_ID";
    public static final String BOARD_NAME = "TEST_BOARD_NAME";
    public static final String TEST_USERNAME = "TEST_USERNAME";
    public static final String TEST_STORY = "TEST_STORY";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private KanbanBoardService kanbanBoardService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void createBoardShouldSucceed() throws Exception {
        CreateKanbanBoardDTO createKanbanBoardDTO = new CreateKanbanBoardDTO(USER_ID, BOARD_NAME);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.BOARD_CREATED_SUCCESSFULLY);
        kanbanBoardResponseMock.setBoard(new KanbanBoard(BOARD_NAME,USER_ID));

        when(kanbanBoardService.createKanbanBoard(any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.OK)
        );

        ResultActions response = mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(KanbanBoardService.BOARD_CREATED_SUCCESSFULLY,kanbanBoardResponse.getMessage());
        assertEquals(USER_ID,kanbanBoardResponse.getBoard().getOwnerID());
        assertEquals(BOARD_NAME,kanbanBoardResponse.getBoard().getName());
    }

    @Test
    public void createBoardShouldFail() throws Exception {
        CreateKanbanBoardDTO createKanbanBoardDTO = new CreateKanbanBoardDTO(USER_ID, BOARD_NAME);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.FAIL_TO_CREATE_BOARD);

        when(kanbanBoardService.createKanbanBoard(any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.CONFLICT)
        );

        ResultActions response = mockMvc.perform(post("/boards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(KanbanBoardService.FAIL_TO_CREATE_BOARD,kanbanBoardResponse.getMessage());
        assertNull(kanbanBoardResponse.getBoard());
    }

    @Test
    public void addUserBoardShouldSucceed() throws Exception {
        AddUserKanbanBoardDTO addUserKanbanBoardDTO = new AddUserKanbanBoardDTO(USER_ID);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.USER_ADDED_SUCCESSFULLY);
        KanbanBoard kanbanBoard = new KanbanBoard(BOARD_NAME, USER_ID);
        kanbanBoard.addUser(USER_ID, TEST_USERNAME);
        kanbanBoardResponseMock.setBoard(kanbanBoard);

        when(kanbanBoardService.addUserToBoard(any(),any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.OK)
        );

        ResultActions response = mockMvc.perform(post("/boards/1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(KanbanBoardService.USER_ADDED_SUCCESSFULLY,kanbanBoardResponse.getMessage());
        assertTrue(kanbanBoardResponse.getBoard().getParticipants().containsKey(USER_ID));
    }

    @Test
    public void addUserBoardShouldFailUserAlreadyAdded() throws Exception {
        AddUserKanbanBoardDTO addUserKanbanBoardDTO = new AddUserKanbanBoardDTO(USER_ID);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.USER_ALREADY_ADDED);

        when(kanbanBoardService.addUserToBoard(any(),any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.CONFLICT)
        );

        ResultActions response = mockMvc.perform(post("/boards/1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(KanbanBoardService.USER_ALREADY_ADDED,kanbanBoardResponse.getMessage());
    }

    @Test
    public void addUserBoardShouldFailEntityNotFound() throws Exception {
        AddUserKanbanBoardDTO addUserKanbanBoardDTO = new AddUserKanbanBoardDTO(USER_ID);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.ENTITY_NOT_FOUND);

        when(kanbanBoardService.addUserToBoard(any(),any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.NOT_FOUND)
        );

        ResultActions response = mockMvc.perform(post("/boards/1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,kanbanBoardResponse.getMessage());
    }

    @Test
    public void removeUserBoardShouldSucceed() throws Exception {

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.USER_REMOVED_SUCCESSFULLY);
        KanbanBoard kanbanBoard = new KanbanBoard(BOARD_NAME, USER_ID);
        kanbanBoardResponseMock.setBoard(kanbanBoard);

        when(kanbanBoardService.removeUserFromBoard("1",USER_ID)).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.OK)
        );

        ResultActions response = mockMvc.perform(delete("/boards/1/users/USER_ID")
                .contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(KanbanBoardService.USER_REMOVED_SUCCESSFULLY,kanbanBoardResponse.getMessage());
        assertFalse(kanbanBoardResponse.getBoard().getParticipants().containsKey(USER_ID));
    }

    @Test
    public void removeUserBoardShouldFail() throws Exception {
        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.ENTITY_NOT_FOUND);
        KanbanBoard kanbanBoard = new KanbanBoard(BOARD_NAME, USER_ID);
        kanbanBoard.addUser(USER_ID,TEST_USERNAME);
        kanbanBoardResponseMock.setBoard(kanbanBoard);

        when(kanbanBoardService.removeUserFromBoard("1",USER_ID)).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.NOT_FOUND)
        );

        ResultActions response = mockMvc.perform(delete("/boards/1/users/USER_ID")
                .contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,kanbanBoardResponse.getMessage());
        assertTrue(kanbanBoardResponse.getBoard().getParticipants().containsKey(USER_ID));
    }

    @Test
    public void addCardBoardShouldSucceed() throws Exception {
        AddCardKanbanBoardDTO addUserKanbanBoardDTO = new AddCardKanbanBoardDTO(TEST_STORY);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.CARD_ADDED_SUCCESSFULLY);
        KanbanBoard kanbanBoard = new KanbanBoard(BOARD_NAME, USER_ID);
        kanbanBoardResponseMock.setBoard(kanbanBoard);

        when(kanbanBoardService.addCardToBoard(any(),any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.OK)
        );

        ResultActions response = mockMvc.perform(post("/boards/1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(KanbanBoardService.CARD_ADDED_SUCCESSFULLY,kanbanBoardResponse.getMessage());
    }

    @Test
    public void addCardBoardShouldFailEntityNotFound() throws Exception {
        AddCardKanbanBoardDTO addUserKanbanBoardDTO = new AddCardKanbanBoardDTO(TEST_STORY);

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.ENTITY_NOT_FOUND);

        when(kanbanBoardService.addCardToBoard(any(),any())).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.NOT_FOUND)
        );

        ResultActions response = mockMvc.perform(post("/boards/1/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addUserKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,kanbanBoardResponse.getMessage());
    }

    @Test
    public void removeCardBoardShouldSucceed() throws Exception {

        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.CARD_REMOVED_SUCCESSFULLY);
        KanbanBoard kanbanBoard = new KanbanBoard(BOARD_NAME, USER_ID);
        kanbanBoardResponseMock.setBoard(kanbanBoard);

        when(kanbanBoardService.removeCardFromBoard("1","1")).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.OK)
        );

        ResultActions response = mockMvc.perform(delete("/boards/1/cards/1")
                .contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(KanbanBoardService.CARD_REMOVED_SUCCESSFULLY,kanbanBoardResponse.getMessage());
    }

    @Test
    public void removeCardBoardShouldFail() throws Exception {
        KanbanBoardResponse kanbanBoardResponseMock = new KanbanBoardResponse(KanbanBoardService.ENTITY_NOT_FOUND);

        when(kanbanBoardService.removeCardFromBoard("1","1")).thenReturn(
                new ResponseEntity<>(kanbanBoardResponseMock, HttpStatus.NOT_FOUND)
        );

        ResultActions response = mockMvc.perform(delete("/boards/1/cards/1")
                .contentType(MediaType.APPLICATION_JSON));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
        assertEquals(KanbanBoardService.ENTITY_NOT_FOUND,kanbanBoardResponse.getMessage());
    }
}