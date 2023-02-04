package com.example.Kanban.UserProfile;

import java.util.Objects;

public class KanbanBoardReference {
    private String kanbanBoardID;
    private String kanbanBoardName;

    public KanbanBoardReference(String kanbanBoardID, String kanbanBoardName) {
        this.kanbanBoardID = kanbanBoardID;
        this.kanbanBoardName = kanbanBoardName;
    }

    public String getKanbanBoardID() {
        return kanbanBoardID;
    }

    public String getKanbanBoardName() {
        return kanbanBoardName;
    }

    public void setKanbanBoardName(String kanbanBoardName) {
        this.kanbanBoardName = kanbanBoardName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KanbanBoardReference that = (KanbanBoardReference) o;
        return Objects.equals(kanbanBoardID, that.kanbanBoardID) && Objects.equals(kanbanBoardName, that.kanbanBoardName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kanbanBoardID, kanbanBoardName);
    }
}
