package com.pro.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtTokenDto {
    private String grantType; // jwt에 대한 인증 타입(Bearer)
    private String accessToken; // 사용자 요청에 사용할 실 jwt
    private String refreshToken; // 재발급용
}
