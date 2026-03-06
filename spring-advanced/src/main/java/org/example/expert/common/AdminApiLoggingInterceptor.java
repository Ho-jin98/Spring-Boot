package org.example.expert.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminApiLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        // request에서 JwtFilter에 저장해둔 userRole 꺼내기
        String userRole = (String) request.getAttribute("userRole");

        // Admin 권한 확인
        if (!"ADMIN".equals(userRole)) {
//            throw new SecurityException("ADMIN 권한이 없습니다.");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin 권한이 없습니다.");
            return false;
        }
        /*String path = request.getRequestURI();
        String decodePath = URLDecoder.decode(path, StandardCharsets.UTF_8);

        String[] paths = decodePath.split("/");
        String userId = paths[paths.length - 2];*/


        // 로깅 (요청 시각 + URL)
        log.info("ADMIN API 요청 - 시각: {}, URL: {}",
                LocalDateTime.now(),
                request.getRequestURI());

        return true; // 통과
    }
}
