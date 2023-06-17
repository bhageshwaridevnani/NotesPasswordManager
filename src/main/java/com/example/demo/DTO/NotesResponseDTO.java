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
public class NotesResponseDTO extends BaseDTO{

    private String title;

    private String noteContent;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean isSecure;

    private NotesCategory category;

}
