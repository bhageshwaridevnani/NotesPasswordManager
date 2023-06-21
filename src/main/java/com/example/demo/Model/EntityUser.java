package com.example.demo.Model;

import com.example.demo.DTO.RefreshTokenDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Email;
import java.util.Date;

@Getter(AccessLevel.PUBLIC)
@Setter
@FieldNameConstants
@NoArgsConstructor
@Document
public class EntityUser extends DateAudit implements Persistable<String> {

    @Id
    private String id;

    @Field("firstName")
    private String firstName = "";

    @Field("lastName")
    private String lastName = "";

    @Field("password")
    private String password;

    @Email(message = "Invalid email format")
    private String email;

    private String phoneNumber;

    private String userName;

    private String masterPassword;

    private String otp;

    private Date optGenerateDate;

    private long userId;

    private RefreshTokenDTO refreshToken;

    @Override
    public boolean isNew() {
        return (getId() == null);
    }
}
