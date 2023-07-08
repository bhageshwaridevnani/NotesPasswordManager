package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
public class SignUpDTO extends BaseDTO{

    private String id;

    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;

    private String password;

    private String confirmPassword;

    private String masterPassword;

    private String confirmMasterPassword;
}
