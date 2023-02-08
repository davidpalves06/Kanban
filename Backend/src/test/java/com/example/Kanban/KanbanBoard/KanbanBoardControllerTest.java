package com.example.Kanban.KanbanBoard;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = KanbanBoardController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class KanbanBoardControllerTest {
    public static final String USER_ID = "USER_ID";
    public static final String BOARD_NAME = "TEST_BOARD_NAME";
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

        ResultActions response = mockMvc.perform(post("/boards/create")
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

        ResultActions response = mockMvc.perform(post("/boards/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createKanbanBoardDTO)));

        byte[] contentAsByteArray = response.andReturn().getResponse().getContentAsByteArray();
        KanbanBoardResponse kanbanBoardResponse = objectMapper.readValue(contentAsByteArray, KanbanBoardResponse.class);

        response.andExpect(MockMvcResultMatchers.status().isConflict());
        assertEquals(KanbanBoardService.FAIL_TO_CREATE_BOARD,kanbanBoardResponse.getMessage());
        assertNull(kanbanBoardResponse.getBoard());
    }
}