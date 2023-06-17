package com.example.demo.Model;

import com.example.demo.Enum.NotesCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityNotes extends DateAudit {

    @Id
    private String id;

    private long userId;

    private String title;

    private NotesCategory category;

    private String noteContent;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean secure = false;

    @Field("isDeleted")
    @JsonProperty("isDeleted")
    private boolean deleted = false;
}
