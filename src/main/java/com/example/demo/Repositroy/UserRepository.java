package com.example.demo.Repositroy;

import com.example.demo.Model.EntityUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<EntityUser, Integer> {

    EntityUser findByUserName(String username);


    Optional<EntityUser> findByEmail(String email);

    EntityUser findByUserId(long userId);

    List<EntityUser> findByCreatedAtBefore(LocalDateTime thirtyDaysAgo);
}
