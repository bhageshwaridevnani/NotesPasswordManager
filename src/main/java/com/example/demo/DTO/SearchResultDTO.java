package com.example.demo.DTO;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchResultDTO<T> {

    private List<T> list;

    private long totalRecords;

    private int totalPages;

    public SearchResultDTO(List<T> list, long totalRecords, int pageSize) {
        setList(list);
        setTotalRecords(totalRecords);
        setTotalPages(totalRecords, pageSize);
    }

    public SearchResultDTO() {

    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalPages(Long counter, int pageSize) {

        if (pageSize != 0) {
            double ceil = Math.ceil(counter.doubleValue() / (double) pageSize);
            this.totalPages = (int) ceil;
        } else {
            this.totalPages = 0;
        }
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }
}
