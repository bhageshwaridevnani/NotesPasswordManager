package com.example.demo.Repositroy;

import com.example.demo.Model.EntityNotes;
import com.example.demo.Model.EntityUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository  extends MongoRepository<EntityNotes, String> {
}
