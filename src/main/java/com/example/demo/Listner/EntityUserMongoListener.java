package com.example.demo.Listner;

import com.example.demo.Model.EntityDatabaseSequence;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class EntityUserMongoListener extends AbstractMongoEventListener<EntityUser> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(BeforeConvertEvent<EntityUser> event) {
        super.onBeforeConvert(event);
        EntityUser entityUser = event.getSource();
        EntityDatabaseSequence counter = null;
        long count = 0L;
        if (entityUser.isNew()) {
            counter = mongoTemplate.findAndModify(new Query(Criteria.where("userId").is("USERID")), new Update().inc("userSeq", 1), EntityDatabaseSequence.class);
            count = !Objects.isNull(counter) ? counter.getUserSeq() : 100000;
            entityUser.setUserId(count);
        }
    }
}

