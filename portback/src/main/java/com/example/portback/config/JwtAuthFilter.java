package com.example.portback.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean requiresAuth = false;

        // /api/admin/** 중 로그인 제외하고 인증 필요
        if (path.startsWith("/api/admin/") && !path.equals("/api/admin/login")) {
            requiresAuth = true;
        }
        // /api/projects 쓰기 작업 (POST, PUT, DELETE) 인증 필요
        else if (path.startsWith("/api/projects") &&
                !HttpMethod.GET.name().equals(method) &&
                !HttpMethod.OPTIONS.name().equals(method)) {
            requiresAuth = true;
        }

        if (requiresAuth) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                sendUnauthorized(response, "Unauthorized");
                return;
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                sendUnauthorized(response, "Invalid or expired token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
