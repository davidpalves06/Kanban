package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.cards.CardComment;
import com.example.Kanban.KanbanBoard.cards.KanbanCard;
import com.example.Kanban.KanbanBoard.cards.ProgressStatus;

import java.util.LinkedList;
import java.util.List;

public class KanbanBoard {
    private String id;
    private final String name;
    private final String ownerID;
    private List<UserReference> participantsID;
    private List<KanbanCard> boardCards;

    public KanbanBoard(String name, String ownerID) {
        this.name = name;
        this.ownerID = ownerID;
        this.participantsID = new LinkedList<>();
        this.boardCards = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public List<UserReference> getParticipantsList() {
        return participantsID;
    }

    public List<KanbanCard> getBoardCards() {
        return boardCards;
    }

    public void addUser(UserReference userReference) {
        participantsID.add(userReference);
    }

    public void removeUser(String userID) {
        participantsID.removeIf((userRef) -> userRef.getUserID().equals(userID));
    }

    public void addCard(KanbanCard kanbanCard) {
        boardCards.add(kanbanCard);
    }

    public void removeCard(String cardID) {
        boardCards.removeIf((card) -> card.getId().equals(cardID));
    }

    public void changeCardStatus(String cardID, ProgressStatus status) {
        boardCards.forEach((card) -> {
            if (card.getId().equals(cardID))
                card.setProgressStatus(status);
        });
    }

    public void addCommentToCard(String cardID, CardComment comment) {
        boardCards.forEach((card) -> {
            if (card.getId().equals(cardID)) card.addComment(comment);
        });
    }

    public void removeCommentFromCard(String cardID, int index) {
        boardCards.forEach((card) -> {
            if (card.getId().equals(cardID)) card.removeComment(index);
        });
    }
}
