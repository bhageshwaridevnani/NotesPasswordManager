package com.example.demo.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
@NoArgsConstructor
public class EntityDatabaseSequence {

    @Id
    private String id;

    private long seq;

    private String userId;

    private long userSeq;
}
