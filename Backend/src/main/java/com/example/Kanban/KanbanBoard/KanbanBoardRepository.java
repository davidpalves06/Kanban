package com.example.Kanban.KanbanBoard;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KanbanBoardRepository extends MongoRepository<KanbanBoard,String> {
}
