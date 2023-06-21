package com.example.demo.Service;

import com.example.demo.DTO.RefreshTokenDTO;
import com.example.demo.DTO.RequestRefreshTokenDTO;
import com.example.demo.DTO.TokenResponseDTO;
import com.example.demo.Exception.BusinessValidationException;
import com.example.demo.Model.EntityUser;
import com.example.demo.Repositroy.UserRepository;
import com.example.demo.Security.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.awt.image.TileObserver;
import java.net.CacheRequest;
import java.time.Instant;
import java.util.UUID;

@Component
public class RefreshTokenService extends BaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    public RefreshTokenService(ModelMapper mapper) {
        super(mapper);
    }

    public String generateRefreshToken(EntityUser entityUser) {
        RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .token(UUID.randomUUID().toString())
                .expirationTime(Instant.now().plusMillis(180000))
                .build();
        entityUser.setRefreshToken(refreshTokenDTO);
        userRepository.save(entityUser);
        return refreshTokenDTO.getToken();
    }

    public TokenResponseDTO validateToken(RequestRefreshTokenDTO refreshTokenDTO) {
        EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where("refreshToken.token").is(refreshTokenDTO.getRefreshToken())
                .and(EntityUser.Fields.email).is(refreshTokenDTO.getEmail())), EntityUser.class);
        if (entityUser != null) {
            if (entityUser.getRefreshToken().getExpirationTime().compareTo(Instant.now()) < 0) {
                entityUser.setRefreshToken(new RefreshTokenDTO());
                userRepository.save(entityUser);
                throw new BusinessValidationException("Refresh token is expired.");
            }
            return TokenResponseDTO.builder()
                    .jwtToken(jwtUtil.generateToken1(refreshTokenDTO.getEmail(), refreshTokenDTO.getPassword(), entityUser.getUserId()))
                    .refreshToken(refreshTokenDTO.getRefreshToken())
                    .build();
        }
        throw new BusinessValidationException("User not found.");
    }
}
