package com.example.demo.Controller;

import com.example.demo.DTO.*;
import com.example.demo.Model.AuthRequest;
import com.example.demo.Repositroy.UserRepository;
import com.example.demo.Security.JwtUtil;
import com.example.demo.Service.UserService;
import com.example.demo.Util.Constant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController extends BaseController{
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


//    @PostMapping("/authenticate")
//    public String generateToken(@RequestBody AuthRequest authrequest) throws Exception {
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authrequest.getUserName(), authrequest.getPassword()));
//        } catch (Exception e) {
//            throw new Exception("Invalid username and password");
//        }
//
//        return jwtUtil.generateToken(authrequest.getUserName());
//    }

    @PostMapping("/signUp")
    @ApiOperation(value = "Sing Up user",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> signUp(@Validated @RequestBody SignUpDTO signUpDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(userService.signUp(signUpDTO,bindingResult), "User sign up successfully.");
    }

    @PostMapping("/login")
    @ApiOperation(value = "Login user",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(userService.login(loginDTO,bindingResult), "User login successfully.");
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "Reset Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO, BindingResult bindingResult) throws Exception {
        userService.resetPassword(resetPasswordDTO,bindingResult);
        return okSuccessResponse("User password reset successfully.");
    }

    @PostMapping("/forgotPassword")
    @ApiOperation(value = "Forgot Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> forgotPassword(@Validated @RequestBody ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult) throws Exception {
        userService.forgotPassword(forgotPasswordDTO,bindingResult);
        return okSuccessResponse("OTP send to your email successfully");
    }

    @PostMapping("/verifyOtp")
    @ApiOperation(value = "Verify Password",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> verifyPassword(@Validated @RequestBody ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult) throws Exception {
        userService.verifyOtpAndSetNewPassword(forgotPasswordDTO,bindingResult);
        return okSuccessResponse("Password reset successfully with otp verified");
    }

    @PostMapping("/updateUserProfile")
    @ApiOperation(value = "Update profile",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> updateProfile(@Validated @RequestBody UpdateProfileDTO updateProfileDTO, BindingResult bindingResult) throws Exception {
        return okSuccessResponse(userService.updateProfile(updateProfileDTO,bindingResult),"Profile Update Successfully");
    }
}
