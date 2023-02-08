package com.example.Kanban.KanbanBoard.cards;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class KanbanCard {
    private String id = UUID.randomUUID().toString();
    private final String userStory;
    private ProgressStatus progressStatus;
    private List<CardComment> comments;

    public KanbanCard(String userStory, ProgressStatus progressStatus) {
        this.userStory = userStory;
        this.progressStatus = progressStatus;
        this.comments = new LinkedList<>();
    }

    public String getId() {
        return id;
    }

    public String getUserStory() {
        return userStory;
    }

    public List<CardComment> getComments() {
        return comments;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    public void addComment(CardComment comment) {
        comments.add(comment);
    }

    public void removeComment(int index) {
        comments.remove(index);
    }
}
