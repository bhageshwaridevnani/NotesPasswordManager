package com.example.demo.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListDTO {

    private String search;

    private SortBy sortBy;

    private String filter;

    private Integer pageId;

    private Integer limit;
}
