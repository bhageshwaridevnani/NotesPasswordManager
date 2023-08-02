package com.example.demo.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class TokenResponseDTO {

    private String jwtToken;

    private String refreshToken;

    private long userId;

    public TokenResponseDTO(String jwtToken, String refreshToken, long userId) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }
}
