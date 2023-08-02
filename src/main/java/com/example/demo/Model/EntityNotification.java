package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class EntityNotification extends DateAudit{

    @Id
    private String id;

    private long userId;

    private String message;

    private String title;

    private boolean isRead;

    @Field("isDeleted")
    @JsonProperty("isDeleted")
    private boolean deleted = false;


    public EntityNotification(long userId, String message, String title, boolean isRead) {
        this.userId = userId;
        this.message = message;
        this.title = title;
        this.isRead = isRead;
    }
}
