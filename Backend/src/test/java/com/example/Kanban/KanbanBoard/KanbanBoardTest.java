package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.cards.CardComment;
import com.example.Kanban.KanbanBoard.cards.KanbanCard;
import com.example.Kanban.KanbanBoard.cards.ProgressStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KanbanBoardTest {

    KanbanBoard kanbanBoard;
    @BeforeEach
    public void setup() {
        kanbanBoard = new KanbanBoard("TestBoard","1");
    }

    @Test
    public void addCard() {
        KanbanCard kanbanCard = new KanbanCard("Testing Adding Card", ProgressStatus.NEW);

        kanbanBoard.addCard(kanbanCard);

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals("Testing Adding Card",kanbanBoard.getBoardCards().get(0).getUserStory());
    }

    @Test
    public void removeCard() {
        KanbanCard kanbanCard1 = new KanbanCard("Testing Adding Card 1", ProgressStatus.NEW);
        KanbanCard kanbanCard2 = new KanbanCard("Testing Adding Card 2", ProgressStatus.NEW);
        kanbanBoard.addCard(kanbanCard1);
        kanbanBoard.addCard(kanbanCard2);

        kanbanBoard.removeCard(kanbanCard2.getId());

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals("Testing Adding Card 1",kanbanBoard.getBoardCards().get(0).getUserStory());
    }

    @Test
    public void addUser() {
        UserReference userRef = new UserReference("1", "TEST_USERNAME");

        kanbanBoard.addUser(userRef);

        assertEquals(1,kanbanBoard.getParticipantsList().size());
        assertEquals("TEST_USERNAME",kanbanBoard.getParticipantsList().get(0).getUsername());
    }

    @Test
    public void removeUser() {
        UserReference userRef1 = new UserReference("1", "TEST_USERNAME");
        UserReference userRef2 = new UserReference("2", "TEST_USERNAME_2");

        kanbanBoard.addUser(userRef1);
        kanbanBoard.addUser(userRef2);

        kanbanBoard.removeUser("1");

        assertEquals(1,kanbanBoard.getParticipantsList().size());
        assertEquals("TEST_USERNAME_2",kanbanBoard.getParticipantsList().get(0).getUsername());
    }

    @Test
    public void changeCardStatus() {
        KanbanCard kanbanCard = new KanbanCard("Testing Adding Card", ProgressStatus.NEW);

        kanbanBoard.addCard(kanbanCard);

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals(ProgressStatus.NEW,kanbanBoard.getBoardCards().get(0).getProgressStatus());

        kanbanBoard.changeCardStatus(kanbanCard.getId(),ProgressStatus.IN_PROGRESS);

        assertEquals(ProgressStatus.IN_PROGRESS,kanbanBoard.getBoardCards().get(0).getProgressStatus());

    }

    @Test
    public void addCommentToCard() {
        KanbanCard kanbanCard = new KanbanCard("Testing Adding Card", ProgressStatus.NEW);

        kanbanBoard.addCard(kanbanCard);

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals(0,kanbanBoard.getBoardCards().get(0).getComments().size());

        kanbanBoard.addCommentToCard(kanbanCard.getId(),new CardComment("TEST_USERNAME","TEST_COMMENT"));

        assertEquals(1,kanbanBoard.getBoardCards().get(0).getComments().size());
        assertEquals("TEST_USERNAME",kanbanBoard.getBoardCards().get(0).getComments().get(0).getUsername());
        assertEquals("TEST_COMMENT",kanbanBoard.getBoardCards().get(0).getComments().get(0).getCommentText());
    }

    @Test
    public void removeCommentFromCard() {
        KanbanCard kanbanCard = new KanbanCard("Testing Adding Card", ProgressStatus.NEW);

        kanbanBoard.addCard(kanbanCard);

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals(0,kanbanBoard.getBoardCards().get(0).getComments().size());

        kanbanBoard.addCommentToCard(kanbanCard.getId(),new CardComment("TEST_USERNAME","TEST_COMMENT"));

        kanbanBoard.removeCommentFromCard(kanbanCard.getId(), 0);

        assertEquals(1,kanbanBoard.getBoardCards().size());
        assertEquals(0,kanbanBoard.getBoardCards().get(0).getComments().size());

    }
}