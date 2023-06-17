package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mongodb.morphia.annotations.PrePersist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class DateAudit extends BaseModel{

    //@CreationTimestamp
    @JsonProperty("createdAt")
    @CreatedDate
    private Date createdAt;

    //@UpdateTimestamp
    @JsonProperty("updatedAt")
    @LastModifiedDate
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
