package com.example.Kanban.KanbanBoard;

public class KanbanBoard {
    private String id;
    private String name;

    public KanbanBoard(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
