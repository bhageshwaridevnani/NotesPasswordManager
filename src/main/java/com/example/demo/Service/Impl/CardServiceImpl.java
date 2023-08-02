package com.example.demo.Service.Impl;

import com.example.demo.Base.ResponseWrapper;
import com.example.demo.Client.CardClient;
import com.example.demo.DTO.CardDTO;
import com.example.demo.DTO.IdDTO;
import com.example.demo.DTO.ListDTO;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Service.BaseService;
import com.example.demo.Service.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl extends BaseService implements CardService {

    public CardServiceImpl(ModelMapper mapper) {
        super(mapper);
    }

    @Autowired
    private CardClient cardClient;

    @Override
    public Object addCard(HttpServletRequest request,CardDTO cardDTO) {
        Map<String, Object> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        headers.remove("content-length");
        headers.put("userId",getUserId());
        ResponseWrapper<Object> response = cardClient.addCard(headers, cardDTO);
        if (response.getStatus() == 0)
            throw new BusinessValidationException(response.getMessage());
        return response.getData();
    }

    @Override
    public Object editCard(HttpServletRequest request, CardDTO cardDTO) {
        Map<String, Object> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        headers.remove("content-length");
        headers.put("userId",getUserId());
        ResponseWrapper<Object> response = cardClient.editCard(headers, cardDTO);
        if (response.getStatus() == 0)
            throw new BusinessValidationException(response.getMessage());
        return response.getData();
    }

    @Override
    public void deleteCard(HttpServletRequest request,IdDTO idDTO) {
        Map<String, Object> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        headers.remove("content-length");
        headers.put("userId",getUserId());
        ResponseWrapper<Object> response = cardClient.deleteCard(headers, idDTO);
        if (response.getStatus() == 0)
            throw new BusinessValidationException(response.getMessage());
    }

    @Override
    public Object listCard(HttpServletRequest request,ListDTO listDTO) {
        Map<String, Object> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
        headers.remove("content-length");
        headers.put("userId",getUserId());
        try {

            ResponseWrapper<Object> response = cardClient.listCard(headers, listDTO);
        if (response.getStatus() == 0)
            throw new BusinessValidationException(response.getMessage());
        return response.getData();
        }catch (Exception e){
            throw new BusinessValidationException("Server error");
        }
    }
}
