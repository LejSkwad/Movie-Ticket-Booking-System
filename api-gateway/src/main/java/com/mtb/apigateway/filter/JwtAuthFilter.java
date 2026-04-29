package com.mtb.apigateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtb.apigateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/register",
            "/api/auth/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(response, request, HttpStatus.UNAUTHORIZED,
                    "MISSING_TOKEN", "Vui lòng đăng nhập để tiếp tục");
            return;
        }

        String token = authHeader.substring(7);

        try {
            Claims claims = jwtUtil.extractClaims(token);

            if (isBlacklisted(token)) {
                sendError(response, request, HttpStatus.UNAUTHORIZED,
                        "TOKEN_REVOKED", "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại");
                return;
            }

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);

            HttpServletRequestWrapper mutated = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("X-User-Id".equalsIgnoreCase(name)) return userId;
                    if ("X-User-Role".equalsIgnoreCase(name)) return role;
                    return super.getHeader(name);
                }
            };

            filterChain.doFilter(mutated, response);

        } catch (Exception e) {
            sendError(response, request, HttpStatus.UNAUTHORIZED,
                    "INVALID_TOKEN", "Token không hợp lệ hoặc đã hết hạn");
        }
    }

    //Kiểm tra token trong redis
    private boolean isBlacklisted(String token) {
        try {
            Boolean isBlacklisted = redisTemplate.hasKey("blacklist:" + token);
            return isBlacklisted;
        } catch (Exception e) {
            log.warn("Redis unavailable, skipping blacklist check");
            return false;
        }
    }

    private void sendError(HttpServletResponse response, HttpServletRequest request,
                           HttpStatus status, String error, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status", status.value(),
                "error", error,
                "message", message,
                "path", request.getRequestURI()
        );

        objectMapper.writeValue(response.getWriter(), body);
    }
}
