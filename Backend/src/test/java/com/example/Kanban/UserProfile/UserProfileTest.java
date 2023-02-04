package com.example.Kanban.UserProfile;


import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {

    @Test
    public void addToKanbanBoardTest() {
        UserProfile userProfile = new UserProfile("Test123");
        KanbanBoardReference boardReference = new KanbanBoardReference("1","TestBoard");

        userProfile.addToKanbanBoard(boardReference);

        List<KanbanBoardReference> accountKanbanBoards = userProfile.getUserAccountKanbanBoards();

        assertEquals(1, accountKanbanBoards.size());
        assertTrue(accountKanbanBoards.contains(boardReference));
    }

    @Test
    public void removeFromKanbanBoardTest() {
        UserProfile userProfile = new UserProfile("Test123");
        KanbanBoardReference boardReference = new KanbanBoardReference("1","TestBoard");
        userProfile.addToKanbanBoard(boardReference);
        List<KanbanBoardReference> accountKanbanBoards = userProfile.getUserAccountKanbanBoards();
        assertEquals(1, accountKanbanBoards.size());

        userProfile.removeFromKanbanBoard("1");

        accountKanbanBoards = userProfile.getUserAccountKanbanBoards();
        assertEquals(0, accountKanbanBoards.size());
        assertFalse(accountKanbanBoards.contains(boardReference));
    }
}
