package com.example.demo.Security;

import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
//    private String secret = "xadmin";

    private final String jwtSecret = "JWTSuperSecretKey";


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        // here it will check if the token has created before time limit. i.e 10 hours then will will return true else false
        return extractExpiration(token).before(new Date());
    }

    // this method is for generating token. as argument is username. so as user first time send request with usernamr and password
    // so here we will fetch the username , so based on that username we are going to create one token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // in this method createToken subject argument is username
    // here we are setting the time for 10 hours to expire the token.
    // and you can see we are using HS256 algorithmn
    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
        

    }

    // here we are validation the token
    public Boolean validateToken(String token, UserDetails userDetails) {
        // basically token will be generated in encrpted string and from that string . we extract our usename and password using extractUsername method
        final String username = extractUsername(token);
        // here we are validation the username and then check the token is expired or not
        if (isTokenExpired(token)) {
            throw new RuntimeException("Token is expired");
        } else if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("User not found");
        } else {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }
    }

    @SneakyThrows
    public CustomUserDetail validateAndAuthenticateToken(String authToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken)
                    .getBody();
            String email = claims.get("email").toString();

            User user = new User(email, "", Collections.singleton(new SimpleGrantedAuthority(email)));
            return new CustomUserDetail(user, claims.getSubject());

        } catch (MalformedJwtException ex) {
            throw new Exception("JWT invalid");
        } catch (UnsupportedJwtException ex) {
            throw new Exception("Unsupported jwt token");
        } catch (IllegalArgumentException ex) {
            throw new Exception("String is empty");
        } catch (ExpiredJwtException ex) {
            throw new Exception("JWT expire");
        } catch (Exception e) {
            throw new Exception(e.getLocalizedMessage());
        }
    }

    public String generateToken1(String email, String password,long userId) {
        return createToken1(email, password,userId);
    }

    private String createToken1(String email, String password,long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("password", password);
        claims.put("userId",userId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();

    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        Integer integerNumber = (Integer) claims.get("userId");
        return integerNumber.longValue();
    }
}
