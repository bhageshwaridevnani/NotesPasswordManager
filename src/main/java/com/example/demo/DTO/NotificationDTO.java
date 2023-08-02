package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO extends BaseDTO{

    private long userId;
    private String message;

    private String title;

    private boolean isRead;

    @Field("isDeleted")
    @JsonProperty("isDeleted")
    private boolean deleted = false;
    public NotificationDTO(String message, String title, boolean isRead) {
        this.message = message;
        this.title = title;
        this.isRead = isRead;
    }
}
