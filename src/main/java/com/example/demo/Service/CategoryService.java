package com.example.demo.Service;

import com.example.demo.Model.EntityCategory;

public interface CategoryService {
    Object addCategory(EntityCategory entityCategory);

    Object updateCategory(EntityCategory entityCategory);

    Object deleteCategory(EntityCategory entityCategory);
}
