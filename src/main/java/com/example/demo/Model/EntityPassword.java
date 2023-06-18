package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Null;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class EntityPassword extends DateAudit{

    @Id
    private String id;

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

    @Field("isDeleted")
    @JsonProperty("isDeleted")
    private boolean deleted = false;
}
