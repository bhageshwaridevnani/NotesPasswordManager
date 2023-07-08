package com.example.demo.Service;

import com.example.demo.DTO.ListDTO;
import com.example.demo.DTO.PasswordDTO;
import org.springframework.stereotype.Service;

@Service
public interface PasswordService {
    Object addPassword(PasswordDTO passwordDTO);

    Object updatePassword(PasswordDTO passwordDTO);

    void deletePassword(PasswordDTO passwordDTO);

    String generatePassword();

    Object openPassword(PasswordDTO passwordDTO);

    Object listPassword(ListDTO listDTO);
}
