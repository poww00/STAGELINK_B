package com.pro.user;

import com.pro.entity.Gender;
import com.pro.entity.Member;
import com.pro.entity.MemberState;
import com.pro.repository.MemberRepository;
import com.pro.security.JwtTokenProvider;
import com.pro.security.jwt.JwtTokenDto;
import com.pro.service.MemberService;
import com.pro.service.RefreshTokenService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 성공 시 accessToken과 refreshToken 발급")
    void 로그인() {
        // Given
        Member member = Member.builder()
                .userId("hong1234")
                .password(passwordEncoder.encode("abc1234"))
                .name("홍길동")
                .nickname("길동이")
                .birthday(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .userEmail("hong1234@email.com")
                .memberStatus(MemberState.ACTIVE)
                .build();

        memberRepository.save(member);

        // When
        JwtTokenDto tokenDto = memberService.login("hong1234", "abc1234");

        // Then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());

        // accessToken 안에 들어있는 userId 검증
        String userIdFromToken = jwtTokenProvider.getUserIdFromToken(tokenDto.getAccessToken());
        assertEquals("hong1234", userIdFromToken);
    }

    @Test
    @DisplayName("로그인 실패: 비밀번호 틀림")
    void 로그인_실패1() {
        // Given
        Member member = Member.builder()
                .userId("hong1234")
                .password(passwordEncoder.encode("abc1234"))
                .name("홍길동")
                .nickname("길동이")
                .birthday(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .userEmail("hong1234@email.com")
                .memberStatus(MemberState.ACTIVE)
                .build();

        memberRepository.save(member);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> {
                    memberService.login("hong1234", "wrongPW");
                });
    }

    @Test
    @DisplayName("로그인 실패: 존재하지 않는 유저")
    void 로그인_실패2() {
        // Then
        assertThrows(UsernameNotFoundException.class,
                () -> {
                    memberService.login("notExist", "abc1234");
                });
    }

    @Test
    @DisplayName("로그아웃: 토큰 제거")
    void 로그아웃() {
        // Given
        String userId = "hong1234";
        String token = "dummy-refresh-token";
        refreshTokenService.save(userId, token);

        // When
        refreshTokenService.delete(userId);

        // Then
        assertNull(refreshTokenService.findRefreshToken(userId));
    }

    @Test
    @DisplayName("refreshToken을 통해 새 accessToken을 재발급한다")
    void 토큰_재발급() {
        // Given: userId로 토큰 생성
        String userId = "hong1234";
        JwtTokenDto oldToken = jwtTokenProvider.generateToken(userId);
        String refreshToken = oldToken.getRefreshToken();

        // When: refreshToken 유효성 검사 및 사용자 ID 추출
        assertTrue(jwtTokenProvider.validateToken(refreshToken));
        String parsedUserId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        assertEquals(userId, parsedUserId);

        // Then: 새 토큰 발급 및 null 아님 확인
        JwtTokenDto newToken = jwtTokenProvider.generateToken(parsedUserId);
        assertNotNull(newToken.getAccessToken());
        assertNotNull(newToken.getRefreshToken());
    }
}