package com.pro.service;

import com.pro.dto.MemberRegisterDto;
import com.pro.dto.ResetPasswordDto;
import com.pro.entity.Member;
import com.pro.entity.MemberState;
import com.pro.repository.MemberRepository;
import com.pro.security.JwtTokenProvider;
import com.pro.security.jwt.JwtTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

public interface MemberService {

    void register(MemberRegisterDto dto);
    Member findByUsername(String username);
    Member login(String userId, String password);

    // 아이디 찾기
    String findUserIdByNameAndEmail(String name, String email);

    // 비밀번호 재설정
    void verifyUserByIdAndEmail(String userId, String userEmail);
    void resetPassword(ResetPasswordDto dto);

    // 회원 탈퇴
    boolean hasReservation(Long userId);
    void withdrawMember(Long userId);

    // 소셜 로그인
    Map<String, Object> getKakaoMember(String accessToken);
    Map<String, Object> getNaverMember(String accessToken, String state);
}
