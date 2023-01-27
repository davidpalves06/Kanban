package com.example.Kanban.UserAccount;


import com.example.Kanban.KanbanBoard.KanbanBoard;
import com.example.Kanban.UserAccount.exception.InvalidUserInfoException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserAccountTest {

    @Test
    public void addToKanbanBoardTest() {
        UserAccount userAccount = new UserAccount("Test123");
        KanbanBoardReference boardReference = new KanbanBoardReference("1","TestBoard");

        userAccount.addToKanbanBoard(boardReference);

        List<KanbanBoardReference> accountKanbanBoards = userAccount.getUserAccountKanbanBoards();

        assertEquals(1, accountKanbanBoards.size());
        assertTrue(accountKanbanBoards.contains(boardReference));
    }

    @Test
    public void removeFromKanbanBoardTest() {
        UserAccount userAccount = new UserAccount("Test123");
        KanbanBoardReference boardReference = new KanbanBoardReference("1","TestBoard");
        userAccount.addToKanbanBoard(boardReference);
        List<KanbanBoardReference> accountKanbanBoards = userAccount.getUserAccountKanbanBoards();
        assertEquals(1, accountKanbanBoards.size());

        userAccount.removeFromKanbanBoard("1");

        accountKanbanBoards = userAccount.getUserAccountKanbanBoards();
        assertEquals(0, accountKanbanBoards.size());
        assertFalse(accountKanbanBoards.contains(boardReference));
    }
}
