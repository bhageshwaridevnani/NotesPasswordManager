package com.example.demo.Client;

import com.example.demo.Base.ResponseWrapper;
import com.example.demo.DTO.CardDTO;
import com.example.demo.DTO.IdDTO;
import com.example.demo.DTO.ListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "cardManage-service")
public interface CardClient {

    @PostMapping("/card")
    ResponseWrapper<Object> addCard(@RequestHeader Map<String, Object> headers,@RequestBody CardDTO cardDTO);
    @PutMapping("/card")
    ResponseWrapper<Object> editCard(@RequestHeader Map<String, Object> headers,@RequestBody CardDTO cardDTO);
    @DeleteMapping("/card")
    ResponseWrapper<Object> deleteCard(@RequestHeader Map<String, Object> headers,@RequestBody IdDTO idDTO);
    @PostMapping("/listCard")
    ResponseWrapper<Object> listCard(@RequestHeader Map<String, Object> headers,@RequestBody ListDTO listDTO);
}
