package com.example.demo.Service;

import com.example.demo.DTO.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


public interface UserService {
    Object signUp(SignUpDTO signUpDTO, BindingResult bindingResult);

    TokenResponseDTO login(LoginDTO loginDTO,BindingResult bindingResult);

    void resetPassword(ResetPasswordDTO resetPasswordDTO,BindingResult bindingResult);

    void forgotPassword(ForgotPasswordDTO forgotPasswordDTO,BindingResult bindingResult);

    void verifyOtpAndSetNewPassword(ForgotPasswordDTO forgotPasswordDTO, BindingResult bindingResult);

    Object updateProfile(UpdateProfileDTO updateProfileDTO, BindingResult bindingResult);
}
