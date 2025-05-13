package com.pro.security;

import com.pro.security.jwt.JwtTokenDto;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    // 시크릿키
    private static final String SECRET_KEY = "12345678901234567890123456789012345678901234567890";

    private Key key;
    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60; //1시간
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; //7일

    // 시크릿 키 기반으로 서명용 key 객체 초기화
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    // AcessToken, RefreshToken 생성
    public JwtTokenDto generateToken(String userId) {
        Date now = new Date();
        Date accessTokenExpires = new Date(now.getTime() + ACCESS_TOKEN_VALIDITY);
        Date refreshTokenExpires = new Date(now.getTime() + REFRESH_TOKEN_VALIDITY);

        String accessToken = Jwts.builder()
                .setSubject(userId) // 사용자 식별 정보
                .setIssuedAt(now)
                .setExpiration(accessTokenExpires) // 만료시간 설정
                .signWith(key, SignatureAlgorithm.HS256) //서명 HS256 알고리즘
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpires)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰으로부터 인증 정보 가져오기
    public Authentication getAuthentication(String token) {
        String userId = getUserIdFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId); // 사용자 정보 조회
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    // 토큰에서 id 추출
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}