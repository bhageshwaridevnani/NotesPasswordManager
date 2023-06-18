package com.example.demo.Controller;

import com.example.demo.DTO.ListDTO;
import com.example.demo.DTO.NotesDTO;
import com.example.demo.DTO.PasswordDTO;
import com.example.demo.Service.PasswordService;
import com.example.demo.Util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "Password API")
public class PasswordController extends BaseController{

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/addPassword")
    @ApiOperation(value = "Add Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> addPassword(@Validated @RequestBody PasswordDTO passwordDTO) throws Exception {
        return okSuccessResponse(passwordService.addPassword(passwordDTO), "Password secure successfully.");
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "Add Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updatePassword(@Validated @RequestBody PasswordDTO passwordDTO) throws Exception {
        return okSuccessResponse(passwordService.updatePassword(passwordDTO), "Password updated successfully.");
    }

    @PostMapping("/deletePassword")
    @ApiOperation(value = "Delete Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deletePassword(@Validated @RequestBody PasswordDTO passwordDTO) throws Exception {
        passwordService.deletePassword(passwordDTO);
        return okSuccessResponse("Password deleted successfully.");
    }

    @PostMapping("/generatePassword")
    @ApiOperation(value = "Generate Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> generatePassword(){
        return okSuccessResponse(passwordService.generatePassword(),"Password generated successfully.");
    }

    @PostMapping("/openPassword")
    @ApiOperation(value = "Open Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> openPassword(@Validated @RequestBody PasswordDTO passwordDTO){
        return okSuccessResponse(passwordService.openPassword(passwordDTO),"Password opened successfully.");
    }

    @PostMapping("/listPassword")
    @ApiOperation(value = "List Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> listPassword(@Validated @RequestBody ListDTO listDTO){
        return okSuccessResponse(passwordService.listPassword(listDTO),"Password list get successfully.");
    }
}
