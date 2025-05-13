package com.pro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Request Header에서 JWT 토큰 추출
        String token = resolveToken(request);

        // validateToken으로 토큰 유효성 검사
        if (token != null) {
            log.debug("추출된 JWT 토큰: {}", token);
        } else {
            log.info("Authorization 헤더에 토큰이 존재하지 않음");
        }

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 완료: {}", authentication.getName());
            }
        } catch (Exception e) {
            // 유효하지 않은 토큰이면 로그 남기고 401 응답
            log.warn("jJWT 인증 실패: {}", e.getMessage());
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다");
//            return; // 필터 진행하지 않고 중단
        }
        filterChain.doFilter(request, response); // 다음 필터로 요청을 전달

    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        //Authorization 헤더에서 "Bearer" 접두사로 시작하는 토큰을 추출하여 반환
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
