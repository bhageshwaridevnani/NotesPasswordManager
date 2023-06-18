package com.example.demo.Repositroy;

import com.example.demo.Model.EntityPassword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends MongoRepository<EntityPassword, String> {
}
