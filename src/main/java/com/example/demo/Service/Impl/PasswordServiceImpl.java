package com.example.demo.Service.Impl;

import com.example.demo.DTO.*;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Model.EntityCategory;
import com.example.demo.Model.EntityNotes;
import com.example.demo.Model.EntityPassword;
import com.example.demo.Repositroy.CategoryRepository;
import com.example.demo.Repositroy.PasswordRepository;
import com.example.demo.Service.BaseService;
import com.example.demo.Service.PasswordService;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class PasswordServiceImpl extends BaseService implements PasswordService {

    public PasswordServiceImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Object addPassword(PasswordDTO passwordDTO) {
        if (StringUtils.isBlank(passwordDTO.getPassword())) {
            throw new BusinessValidationException("Please enter the password");
        }
        if (StringUtils.isNotBlank(passwordDTO.getWebUrl())) {
            passwordDTO.setWebUrl(convertToValidUrl(passwordDTO.getWebUrl()));
        }
        if (!StringUtil.isNullOrEmpty(passwordDTO.getCategory())){
            EntityCategory entityCategory = mongoTemplate.findOne(Query.query(Criteria.where("category").is(passwordDTO.getCategory())),EntityCategory.class);
            if (entityCategory == null){
                entityCategory = new EntityCategory();
                entityCategory.setCategory(passwordDTO.getCategory());
                categoryRepository.save(entityCategory);
            }
        }
        EntityPassword entityPassword = passwordDTO.toModel(EntityPassword.class, getMapper());
        entityPassword.setPassword(getEncryptedPassword(entityPassword.getPassword()));
        passwordRepository.save(entityPassword);
        entityPassword.setUserId(getUserId());
        passwordRepository.save(entityPassword);
        return passwordDTO;
    }

    @Override
    public Object updatePassword(PasswordDTO passwordDTO) {
        EntityPassword entityPassword = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(passwordDTO.getId())), EntityPassword.class);
        if (entityPassword == null) {
            throw new BusinessValidationException("This password not found");
        }
        if (entityPassword.isSecure()) {
            checkSecurity(passwordDTO);
        }
        entityPassword = passwordDTO.toModel(EntityPassword.class, getMapper());
        entityPassword.setUserId(getUserId());
        passwordRepository.save(entityPassword);

        return passwordDTO;
    }

    @Override
    public void deletePassword(PasswordDTO passwordDTO) {
        EntityPassword entityPassword = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(passwordDTO.getId())), EntityPassword.class);
        if (entityPassword == null) {
            throw new BusinessValidationException("This password not found");
        }
        if (entityPassword.isSecure()) {
            checkSecurity(passwordDTO);
        } else {
            entityPassword.setDeleted(true);
            passwordRepository.save(entityPassword);
        }

    }

    @Override
    public String generatePassword() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS, CharacterPredicates.ASCII_LETTERS)
                .build();
        return  generator.generate(8);
    }

    @Override
    public Object openPassword(PasswordDTO passwordDTO) {
        EntityPassword entityPassword = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(passwordDTO.getId())
                .and("userId").is(getUserId())
                .and("isDeleted").is(false)),EntityPassword.class);
        if (entityPassword != null){
            if (entityPassword.isSecure()){
                checkSecurity(passwordDTO);
            }
            return entityPassword.toDTO(PasswordResponseDTO.class,getMapper());
        }
        return null;
    }

    @Override
    public Object listPassword(ListDTO listDTO) {
        Criteria criteria = Criteria.where("userId").is(getUserId())
                .and("isDeleted").is(false);
        if (!StringUtil.isNullOrEmpty(listDTO.getFilter())){
            criteria.and("category").is(listDTO.getFilter());
        }
        if (!StringUtil.isNullOrEmpty(listDTO.getSearch())) {
            criteria.orOperator(Criteria.where("title").regex(listDTO.getSearch(), "i"),
                        Criteria.where("userName").regex(listDTO.getSearch(),"i"),
                        Criteria.where("email").regex(listDTO.getSearch(),"i"));
        }
        Query query = new Query(criteria);
        long totalCount = mongoTemplate.count(query, EntityPassword.class);
        if (totalCount > 1) {
            if (listDTO.getPageId() != null && listDTO.getLimit() != null) {
                int pageId = listDTO.getPageId();
                int limit = listDTO.getLimit();
                query.skip((long) pageId * limit).limit(limit);
            }
            if (listDTO.getSortBy() != null) {
                SortBy sortBy = listDTO.getSortBy();
                Sort sort = Sort.by(sortBy.getDirection(), sortBy.getProperty());
                query.with(sort);
            }
        }
        List<PasswordResponseDTO> passwordResponseDTOS = mongoTemplate.find(query, PasswordResponseDTO.class, "entityPassword");
        return new SearchResultDTO<>(passwordResponseDTOS,totalCount,listDTO.getLimit());
    }

    private void checkSecurity(PasswordDTO passwordDTO) {
        if (StringUtil.isNullOrEmpty(passwordDTO.getMasterPassword())) {
            throw new BusinessValidationException("Please provide master password as this password save with master password");
        }
        if (!isPasswordMatch(passwordDTO.getMasterPassword(), getMasterPassword())) {
            throw new BusinessValidationException("You are providing wrong master password");
        }
    }

    private String convertToValidUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        try {
            URL webUrl = new URL(url);
            // URL is valid, save it
            return webUrl.toString();
        } catch (MalformedURLException e) {
            // Invalid URL, handle the exception
            e.printStackTrace();
        }
        return null;
    }
}
