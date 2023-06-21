package com.example.demo.Controller;

import com.example.demo.DTO.PasswordDTO;
import com.example.demo.DTO.RequestRefreshTokenDTO;
import com.example.demo.Service.RefreshTokenService;
import com.example.demo.Util.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController extends BaseController{

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/refreshToken")
    @ApiOperation(value = "Refresh Token",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> refreshToken(@RequestBody RequestRefreshTokenDTO refreshTokenDTO) throws Exception {
        return okSuccessResponse(refreshTokenService.validateToken(refreshTokenDTO), "Refresh the token.");
    }
}
