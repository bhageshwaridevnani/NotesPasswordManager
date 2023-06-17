package com.example.demo.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
public class EntityPassword extends DateAudit{

    @Id
    private String id;

    private String title;

    private String password;
}
