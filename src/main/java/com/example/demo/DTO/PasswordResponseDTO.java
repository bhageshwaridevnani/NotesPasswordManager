package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class PasswordResponseDTO extends BaseDTO {

    private long userId;

    private String webUrl;

    private String userName;

    private String email;

    private String password;

    private String category;

    private String title;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean isSecure;
}
