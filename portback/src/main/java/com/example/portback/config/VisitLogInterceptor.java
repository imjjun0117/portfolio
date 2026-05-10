package com.example.portback.config;

import com.example.portback.domain.VisitLog;
import com.example.portback.repository.VisitLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class VisitLogInterceptor implements HandlerInterceptor {

    private final VisitLogRepository visitLogRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        // GET 요청만, API/어드민/정적파일 제외
        if (!"GET".equals(method)) return true;
        if (path.startsWith("/api") || path.startsWith("/uploads") || path.startsWith("/assets")) return true;

        String ip = getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");

        visitLogRepository.save(VisitLog.builder()
                .ip(ip)
                .path(path)
                .userAgent(userAgent != null ? userAgent : "")
                .referer(referer != null ? referer : "")
                .build());

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty()) {
            return ip.split(",")[0].trim(); // 프록시 체인에서 첫 번째 IP
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty()) return ip;
        return request.getRemoteAddr();
    }
}
