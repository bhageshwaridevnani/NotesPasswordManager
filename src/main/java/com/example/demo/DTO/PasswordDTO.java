package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@NoArgsConstructor
public class PasswordDTO extends BaseDTO{

    @Id
    private String id;

    private String webUrl;

    private String userName;

    private String email;

    private String password;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean secure;

    private String title;

    private String category;

    @Nullable
    private String masterPassword;
}
