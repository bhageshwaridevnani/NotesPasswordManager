package com.example.demo.DTO;

import com.example.demo.Enum.NotesCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class NotesDTO extends BaseDTO{

    private String id;

    private String title;

    private String noteContent;

    private NotesCategory category;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean secure = false;

    private String masterPassword;

}
