package com.example.Kanban.KanbanBoard;

import com.example.Kanban.KanbanBoard.cards.CardComment;
import com.example.Kanban.KanbanBoard.cards.KanbanCard;
import com.example.Kanban.KanbanBoard.cards.ProgressStatus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KanbanBoard {
    private String id;
    private final String name;
    private final String ownerID;
    private Map<String,String> participants;
    private List<KanbanCard> boardCards;

    public KanbanBoard(String name, String ownerID) {
        this.name = name;
        this.ownerID = ownerID;
        this.participants = new HashMap<>();
        this.boardCards = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public Map<String, String> getParticipants() {
        return participants;
    }

    public List<KanbanCard> getBoardCards() {
        return boardCards;
    }

    public boolean addUser(String userID,String username) {
        if (participants.containsKey(userID)) return false;
        else {
            participants.putIfAbsent(userID,username);
            return true;
        }
    }

    public void removeUser(String userID) {
        participants.remove(userID);
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
