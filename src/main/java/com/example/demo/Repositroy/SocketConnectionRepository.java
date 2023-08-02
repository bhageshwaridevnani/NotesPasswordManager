package com.example.demo.Repositroy;

import com.example.demo.Model.EntitySocketConnection;
import org.springframework.data.mongodb.repository.MongoRepository;

public  interface SocketConnectionRepository extends MongoRepository<EntitySocketConnection, String> {
    void deleteBySessionId(String toString);
}
