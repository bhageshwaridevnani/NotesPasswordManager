package com.example.demo.Security;

;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class ExecutionContextUtil implements Serializable {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private String jwtExpirationInMs;

    private static final long serialVersionUID = -6751490154133933000L;
    // Atomic integer containing the next thread ID to be assigned
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final ThreadLocal<ExecutionContextUtil> EXECUTION_CONTEXT = new ImprovedThreadLocal<ExecutionContextUtil>() {

        @Override
        protected ExecutionContextUtil initialValue() {
            return new ExecutionContextUtil(nextId.getAndIncrement());
        }
    };

    @Autowired
    private final JwtUtil jwtUtil;

    public ExecutionContextUtil(int threadId) {
        this.threadId = threadId;
        this.jwtUtil = new JwtUtil();
    }

    private Long userId;
    private String userName;
    private final int threadId;


    public static synchronized ExecutionContextUtil getContext() {
        if (Objects.isNull(EXECUTION_CONTEXT.get())) {
            EXECUTION_CONTEXT.set(new ExecutionContextUtil(nextId.getAndIncrement()));
        }
        return EXECUTION_CONTEXT.get();
    }

    public static void set(ExecutionContextUtil ExecutionContextUtil) {
        EXECUTION_CONTEXT.set(ExecutionContextUtil);
    }

    public void destroy() {
        EXECUTION_CONTEXT.remove();
    }

    public ExecutionContextUtil init(HttpServletRequest httpRequest) {

        String token = httpRequest.getHeader("X-ACCESS-TOKEN");
        if (StringUtils.isNotEmpty(token)) {
            userName = extractUsername(token);
            userId = extractUserId(token);
        } else {
            userId = Long.valueOf(httpRequest.getHeader("userId"));
        }
        /**
         * This is for tenantId
         */
        return this;
    }

    private Long extractUserId(String token) {
        return jwtUtil.extractUserId(token);
//        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//        return (long) claims.get("userId");
    }

    private String extractUsername(String token) {
        return jwtUtil.extractUsername(token);
//        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//        return claims.get("email").toString();
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getThreadId() {
        return threadId;
    }

    public Long getUserId() {
        return userId;
    }


    public void setUserId(Long userId) {
        this.userId = userId;
    }


}
