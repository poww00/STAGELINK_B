package com.pro.security.user;

import com.pro.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 사용자 인증 정보를 담는 클래스
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER"); // 사용자 권한 하나로 고정
    }

    @Override
    public String getPassword() { // 사용자 비밀번호 반환
        return member.getPassword();
    }

    @Override
    public String getUsername() { // 사용자 아이디 반환
        return member.getUserId(); // 로그인 시 입력한 userId, Security 기준 username 역할
    }

    // 사용자의 계정 상태가 정상인지
    @Override public boolean isAccountNonExpired() {
        return true;
    }
    @Override public boolean isAccountNonLocked() {
        return true;
    }
    @Override public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override public boolean isEnabled() {
        return true;
    }
}
