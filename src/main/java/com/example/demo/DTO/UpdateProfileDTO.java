package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileDTO extends BaseDTO{

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String oldMasterPassword;

    private String masterPassword;

    private String confirmMasterPassword;
}
