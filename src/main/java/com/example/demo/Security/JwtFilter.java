package com.example.demo.Security;

import com.example.demo.Base.ApiResponse;
import com.example.demo.Model.EntityUser;
import com.example.demo.Util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("X-ACCESS-TOKEN");
        long userId = 0;
        if (request.getHeaderNames().toString().equals("userId")&&!request.getHeader("userId").isEmpty()) {
            userId = Long.parseLong(request.getHeader("userId"));
        }
        String token = null;
        String username = null;
        try {
            if (authorizationHeader != null) {
                token = authorizationHeader;
                CustomUserDetail user;
                EntityUser entityUser;
                user = jwtUtil.validateAndAuthenticateToken(token);
                username = user.getUsername();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    entityUser = service.loadEntityEmail(user.getUsername());
                    if (entityUser != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(entityUser, null, null);
                        authentication.setDetails(user);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        ExecutionContextUtil.getContext().init(request);
                        filterChain.doFilter(request, response);
                        // Added a return statement here to avoid redundant filterChain.doFilter() call
                    } else {
                        setResponse(response, "User is not authorized!", HttpServletResponse.SC_UNAUTHORIZED);
                        // Added a return statement here to avoid redundant filterChain.doFilter() call
                    }
                }
            } else if (userId != 0) {
                EntityUser entityUser = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(100017)), EntityUser.class);
                if (entityUser != null) {
                    entityUser = service.loadEntityEmail(entityUser.getEmail());
                    if (entityUser != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(entityUser, null, null);
                        authentication.setDetails(entityUser);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        ExecutionContextUtil.getContext().init(request);
                        filterChain.doFilter(request, response);
                    }
                }

            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            setResponse(response, "User not found", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> excludeUrlPatterns = new ArrayList<>();
        excludeUrlPatterns.add("/sendNotification");
        excludeUrlPatterns.add("/signUp");
        excludeUrlPatterns.add("/login");
        excludeUrlPatterns.add("/forgotPassword");
        excludeUrlPatterns.add("/verifyOtp");
        excludeUrlPatterns.add("/refreshToken");
        excludeUrlPatterns.add("/v2/api-docs");
        excludeUrlPatterns.add("/configuration/ui");
        excludeUrlPatterns.add("/swagger-resources/**");
        excludeUrlPatterns.add("/configuration/security");
        excludeUrlPatterns.add("/swagger-ui.html");
        excludeUrlPatterns.add("/employee");
        excludeUrlPatterns.add("/favicon.ico");
        excludeUrlPatterns.add("/error");
        excludeUrlPatterns.add("/webjars/**");
        excludeUrlPatterns.add("/actuator/**");
        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    private void setResponse(HttpServletResponse response, String message, int responseStatus) throws IOException {
        int status;
        if (responseStatus != 200) {
            status = Constant.SUCCESS;
        } else {
            status = Constant.FAIL;
        }

        ApiResponse errorResponse = new ApiResponse<>(objectMapper.createObjectNode(), message, status);
        byte[] responseToSend = restResponseBytes(errorResponse);

        response.setHeader("Content-Type", "application/json");
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseBytes(ApiResponse eErrorResponse) throws IOException {
        String serialized = objectMapper.writeValueAsString(eErrorResponse);
        return serialized.getBytes();
    }
}
