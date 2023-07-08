package com.example.demo.Service;

import com.example.demo.Model.EntityUser;
import com.example.demo.Security.ExecutionContextUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class BaseService {


    @Autowired
    private MongoTemplate mongoTemplate;

    private final ModelMapper mapper; // Autowire the ModelMapper

    public BaseService(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public ModelMapper getMapper() {
        return mapper;
    }

    protected String getUserName() {
        return ExecutionContextUtil.getContext().getUserName();
    }

    protected long getUserId() {
        return ExecutionContextUtil.getContext().getUserId();
    }

    public String getEncryptedPassword(String password) {
        return encryptPassword(password);
    }


    public boolean isPasswordValid(String password, String encryptedPassword) {
        return isPasswordMatch(password, encryptedPassword);
    }

    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public boolean isPasswordMatch(String password, String encryptedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, encryptedPassword);
    }

    public String getMasterPassword() {
        long userId = ExecutionContextUtil.getContext().getUserId();
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId)), EntityUser.class);
        if (entityUser != null)
            return entityUser.getMasterPassword();
        return null;
    }
//    protected final MessageService messageService;
//    protected final MapperFacade mapper;
//
//    @Autowired
//    private MapperFacade mapper;
//
//
//    public BaseService(MapperFacade mapperFacade) {
//        this.mapper = mapperFacade;
////        this.messageService = mService;
//    }
//
//    protected MapperFacade getMapper() {
//        return mapper;
//    }
//
//
//
//    protected BaseService( MapperFacade mapper) {
//        this.mapper = mapper;
//    }

}
