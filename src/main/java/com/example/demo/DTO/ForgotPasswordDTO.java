package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ForgotPasswordDTO {

    private String email;

    private String otp;

    private String password;

    private String confirmPassword;
}
