package com.pro.controller.member;

import com.pro.dto.LoginRequestDto;
import com.pro.dto.LoginResponseDto;
import com.pro.entity.Member;
import com.pro.repository.MemberRepository;
import com.pro.security.JwtTokenProvider;
import com.pro.security.jwt.JwtTokenDto;
import com.pro.service.MemberService;
import com.pro.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/")
public class AuthController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        try {
            Member member = memberService.login(dto.getUserId(), dto.getPassword());
            JwtTokenDto token = jwtTokenProvider.generateToken(member);

            return ResponseEntity.ok(LoginResponseDto.builder()
                    .accessToken(token.getAccessToken())
                    .refreshToken(token.getRefreshToken())
                    .nickname(member.getNickname())
                    .email(member.getUserEmail())
                    .build());

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404).body("존재하지 않는 사용자입니다.");
        } catch (IllegalStateException e) {
            // 여기서 탈퇴회원은 403으로 처리
            return ResponseEntity.status(403).body("탈퇴한 회원입니다.");
        } catch (IllegalArgumentException e) {
            // 여기만 비밀번호 오류로 제한
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            log.error("로그인 중 알 수 없는 오류 발생", e);
            return ResponseEntity.status(500).body("로그인 처리 중 오류가 발생했습니다.");
        }
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        log.info("로그아웃 요청 도착");
        String token = resolveToken(request);
        log.info("요청 토큰: {}", token);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            refreshTokenService.delete(userId); // 토큰 삭제
        }
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    // 토큰 재발급
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody JwtTokenDto requestDto) {
        String refreshToken = requestDto.getRefreshToken();
        log.info("요청된 Refresh token: {}", refreshToken);

        // refresh token 유효성 검사 (토큰 서명 위조, 만료 여부 확인)
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().body("유효하지 않은 리프레시 토큰입니다.");
        }

        // 토큰에서 userId(=subject) 추출
        String userId = null;
        try{
            userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
            log.info("파싱된 userId: {}", userId);
        } catch (Exception e){
            // 토큰에서 사용자 정보 추출 실패시 예외 처리
            return ResponseEntity.status(401).body("토큰에서 사용자 정보를 읽을 수 없습니다.");
        }
        // userId가 비어 있으면 토큰이 잘못된 것
        if (userId == null) {
            return ResponseEntity.status(401).body("userId 추출 실패: refreshToken이 올바르지 않습니다.");
        }

        // 서버에 저장해둔 refreshToken과 요청된 refreshToken이 일치하는지 확인
        String storedRefreshToken = refreshTokenService.findRefreshToken(userId);
        log.info("서버에 저장된 refreshToken: {}", storedRefreshToken);
        // 저장된 토큰이 없거나, 요청한 토큰과 다른 경우
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(401).body("이미 로그아웃된 사용자이거나 위조된 토큰입니다.");
        }

        // 모든 검증 통과 시 새로운 토큰 발급 & 저장
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음"));

        JwtTokenDto newToken = jwtTokenProvider.generateToken(member);
        refreshTokenService.save(userId, newToken.getRefreshToken());

        return ResponseEntity.ok(newToken);

    }

    // 헤더에서 토큰을 꺼냄
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
