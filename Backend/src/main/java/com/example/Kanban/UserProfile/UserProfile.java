package com.example.Kanban.UserProfile;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Document("user-profile")
public class UserProfile {
    @Id
    private String id;
    private String username;
    private String photo;
    private final List<KanbanBoardReference> userAccountKanbanBoards;

    public UserProfile(String username) {
        this.setUsername(username);
        this.setPhoto("");
        this.userAccountKanbanBoards = new LinkedList<>();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public List<KanbanBoardReference> getUserAccountKanbanBoards() {
        return new ArrayList<>(userAccountKanbanBoards);
    }

    public void addToKanbanBoard(KanbanBoardReference boardReference) {
        userAccountKanbanBoards.add(boardReference);
    }

    public void removeFromKanbanBoard(String id) {
        userAccountKanbanBoards.removeIf((boardReference -> boardReference.getKanbanBoardID().equals(id)));
    }

}
