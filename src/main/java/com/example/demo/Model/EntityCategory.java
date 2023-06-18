package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class EntityCategory extends DateAudit{

    @Id
    private String id;

    private String category;

    @Field("isDeleted")
    @JsonProperty("isDeleted")
    private boolean deleted = false;

}
