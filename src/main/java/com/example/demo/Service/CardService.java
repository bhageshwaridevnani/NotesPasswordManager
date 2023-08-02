package com.example.demo.Service;


import com.example.demo.DTO.CardDTO;
import com.example.demo.DTO.IdDTO;
import com.example.demo.DTO.ListDTO;
import org.bson.Document;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

public interface CardService {
    Object addCard(HttpServletRequest request,CardDTO cardDTO);

    Object editCard(HttpServletRequest request, CardDTO cardDTO);

    void deleteCard(HttpServletRequest request,IdDTO idDTO);

    Object listCard(HttpServletRequest request,ListDTO listDTO);
}
