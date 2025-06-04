package com.pro.security.user;

import com.pro.entity.Member;
import com.pro.entity.MemberState;
import com.pro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // userId를 기준으로 회원 조회
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + userId));

        // 탈퇴한 회원일 경우 로그인 차단
        if (member.getMemberStatus() == MemberState.DELETED) {
            throw new UsernameNotFoundException("탈퇴한 회원입니다: " + userId);
        }

        // 찾은 사용자 정보를 감싼 UserDetails 반환
        return new CustomUserDetails(member);
    }
}
