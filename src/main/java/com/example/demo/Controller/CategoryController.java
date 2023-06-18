package com.example.demo.Controller;

import com.example.demo.DTO.PasswordDTO;
import com.example.demo.Model.EntityCategory;
import com.example.demo.Service.CategoryService;
import com.example.demo.Util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Category API")
public class CategoryController extends BaseController{

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addCategory")
    @ApiOperation(value = "Add Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> addCategory(@Validated @RequestBody EntityCategory entityCategory){
        return okSuccessResponse(categoryService.addCategory(entityCategory), "Category added successfully.");
    }

    @PostMapping("/updateCategory")
    @ApiOperation(value = "Update Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateCategory(@Validated @RequestBody EntityCategory entityCategory){
        return okSuccessResponse(categoryService.updateCategory(entityCategory), "Category updated successfully.");
    }

    @PostMapping("/deleteCategory")
    @ApiOperation(value = "Delete Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteCategory(@Validated @RequestBody EntityCategory entityCategory){
        return okSuccessResponse(categoryService.deleteCategory(entityCategory), "Category deleted successfully.");
    }
}
