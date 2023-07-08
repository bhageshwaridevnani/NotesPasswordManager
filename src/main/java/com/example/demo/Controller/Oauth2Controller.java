package com.example.demo.Controller;

import com.example.demo.DTO.SearchResultDTO;
import com.example.demo.Service.Oauth2Service;
import com.example.demo.Util.Constant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Api(tags = "Oauth API")
public class Oauth2Controller extends BaseController{

    @Autowired
    private Oauth2Service oauth2Service;

    @GetMapping("/oauth2/login")
    @ApiOperation(value = "Login with Oauth2",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ModelAndView initiateOAuth2Login(HttpSession session) throws Exception {
        return oauth2Service.loginWithOauth2(session);
    }

    @GetMapping("/oauth2/callback/google")
    @ApiOperation(value = "Login callback url",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ModelAndView oauth2Callback(HttpServletRequest request) {
        return oauth2Service.oauth2Callback(request);
    }

    @GetMapping("/after/callback")
    @ApiOperation(value = "After callback url",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> handleGoogleCallback(@RequestParam("email") String email, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        System.out.println("Email: " + email);
        return okSuccessResponse(oauth2Service.generateToken(email,firstName,lastName), "User login successfully.");
    }

}
