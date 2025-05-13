package com.pro.user;

import com.pro.security.JwtTokenProvider;
import com.pro.security.jwt.JwtTokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("토큰 생성 시 Access/Refresh 토큰이 생성된다")
    void 토큰_생성() {
        // Given
        String userId = "testUser";

        // When
        JwtTokenDto tokenDto = jwtTokenProvider.generateToken(userId);

        // Then
        Assertions.assertNotNull(tokenDto.getAccessToken());
        Assertions.assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @DisplayName("AccessToken에서 사용자 ID를 정상 추출한다")
    void 토큰_파싱() {
        // Given
        String userId = "testUser";

        // When
        String accessToken = jwtTokenProvider.generateToken(userId).getAccessToken();
        String parsedUserId = jwtTokenProvider.getUserIdFromToken(accessToken);

        // Then
        assertEquals(userId, parsedUserId);
    }

    @Test
    @DisplayName("올바른 토큰은 유효성 검사에서 true를 반환한다")
    void 트큰_유효성_검사1() {
        // Given
        String accessToken = jwtTokenProvider.generateToken("testUser").getAccessToken();
        // When & Then
        assertTrue(jwtTokenProvider.validateToken(accessToken));
    }

    @Test
    @DisplayName("잘못된 토큰은 유효성 검사에서 false를 반환한다")
    void 토큰_유효성_검사() {
        // Given
        String fakeToken = "invalidToken";
        //When & Then
        assertFalse(jwtTokenProvider.validateToken(fakeToken));
    }
}
