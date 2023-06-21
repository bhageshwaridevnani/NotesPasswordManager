package com.example.demo.DTO;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenDTO {

    private String token;

    private Instant expirationTime;
}
