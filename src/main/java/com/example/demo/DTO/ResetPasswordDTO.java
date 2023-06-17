package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;


@Getter
@Setter
@NoArgsConstructor
public class ResetPasswordDTO {

    @Email
    private String email;

    private String password;

    private String newPassword;
}
