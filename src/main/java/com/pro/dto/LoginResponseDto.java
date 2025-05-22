package com.pro.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto { // 응답 DTO
    private String accessToken;
    private String refreshToken;
    private String nickname;
    private String email;
}
