package org.example.expert.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
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
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin 권한이 없습니다.");
            return false;
        }

        // 로깅 (요청 시각 + URL)
        log.info("ADMIN API 요청 - 시각: {}, URL: {}",
                LocalDateTime.now(),
                request.getRequestURI());

        return true; // 통과
    }


    /*TODO : SLF4J 로거의 특별한 문법 : {} 플레이스 홀더
       {}는 SLF4J가 뒤에 오는 인자를 순서대로 치환해주는 특별한 기호!
       log.info("ADMIN API 요청 - 시각: {}, URL: {}",
                    LocalDateTime.now(),
                    request.getRequestURI());
       한마디로 여기서 첫번째 시각의 {}에는 LocalDateTime.now()가 들어가고,
       두번째 URL의 {}에는 request.getRequestURI()가 들어감! */

       /* 주의할 점이 로거니까 가능한 것! String 타입에서는 불가능!
       String str = "시각: {}, URL: {}"; >> XXXXXXX */

}
