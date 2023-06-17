package com.example.demo.Security;

import com.example.demo.Base.ApiResponse;
import com.example.demo.Model.EntityUser;
import com.example.demo.Util.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.text.ParseException;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("X-ACCESS-TOKEN");
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
                        return; // Added a return statement here to avoid redundant filterChain.doFilter() call
                    } else {
                        setResponse(response, "User is not authorized!", HttpServletResponse.SC_UNAUTHORIZED);
                        return; // Added a return statement here to avoid redundant filterChain.doFilter() call
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
        excludeUrlPatterns.add("/signUp");
        excludeUrlPatterns.add("/login");
        excludeUrlPatterns.add("/forgotPassword");
        excludeUrlPatterns.add("/verifyOtp");
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
