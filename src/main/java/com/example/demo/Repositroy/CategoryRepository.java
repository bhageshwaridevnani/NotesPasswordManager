package com.example.demo.Repositroy;

import com.example.demo.Model.EntityCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<EntityCategory, String> {
}
