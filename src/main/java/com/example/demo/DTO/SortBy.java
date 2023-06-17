package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@NoArgsConstructor
public class SortBy {

    private String property = "createdAt";

    private Sort.Direction direction = Sort.Direction.ASC;
}
