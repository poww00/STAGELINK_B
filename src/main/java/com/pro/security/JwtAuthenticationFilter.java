package com.pro.security;

import com.pro.security.user.CustomUserDetails;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            log.debug("추출된 JWT 토큰: {}", token);
        } else {
            log.info("Authorization 헤더에 토큰이 존재하지 않음");
        }

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                //CustomUserDetail 추출
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

                // 탈퇴 회원인지 검사
                if (userDetails.getMember().getMemberStatus().name().equals("DELETED")) {
                    log.warn("탈퇴한 회원의 접근 차단:{}", userDetails.getUsername());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "탈퇴한 회원입니다.");
                    return;
                }

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("인증 완료: {}", authentication.getName());

                /// 회원 pk를 request attribute로 저장
                request.setAttribute("memberNo", userDetails.getId());
            }
        } catch (Exception e) {
            log.warn("JWT 인증 실패: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
