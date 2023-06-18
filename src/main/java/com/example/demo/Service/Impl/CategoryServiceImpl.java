package com.example.demo.Service.Impl;

import com.example.demo.Model.EntityCategory;
import com.example.demo.Repositroy.CategoryRepository;
import com.example.demo.Service.BaseService;
import com.example.demo.Service.CategoryService;
import io.netty.util.internal.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseService implements CategoryService {
    public CategoryServiceImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Object addCategory(EntityCategory entityCategory) {
        if (!StringUtil.isNullOrEmpty(entityCategory.getCategory())) {
            categoryRepository.save(entityCategory);
        }
        return entityCategory;
    }

    @Override
    public Object updateCategory(EntityCategory newCategory) {
        EntityCategory entityCategory = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(newCategory.getId())), EntityCategory.class);
        if (entityCategory != null) {
            entityCategory = newCategory;
            categoryRepository.save(entityCategory);
            return newCategory;
        }
        return null;
    }

    @Override
    public Object deleteCategory(EntityCategory entityCategory) {
        entityCategory = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(entityCategory.getId())), EntityCategory.class);
        if (entityCategory != null) {
            entityCategory.setDeleted(true);
            categoryRepository.save(entityCategory);
            return entityCategory;
        }
        return null;
    }
}
