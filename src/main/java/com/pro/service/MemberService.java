package com.pro.service;


import com.pro.dto.MemberRegisterDto;
import com.pro.entity.Member;
import com.pro.entity.MemberState;
import com.pro.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(MemberRegisterDto dto) {
        // 중복 아이디 검증
        if (memberRepository.existsByUserId(dto.getUserId())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        // 중복 이메일 검증
        if (memberRepository.existsByUserEmail(dto.getUserEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 비밀번호 일치 확인
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호가 일지하지 않습니다.");
        }

        Member member = Member.builder()
                .userId(dto.getUserId())
                .password(passwordEncoder.encode(dto.getPassword())) // 암호화 처리
                .name(dto.getName())
                .birthday(dto.getBirthday())
/*                .postNo(dto.getPostNo())
                .address(dto.getAddress())*/
                .gender(dto.getGender())
                .nickname(dto.getNickname())
                .userEmail(dto.getUserEmail())
                .memberStatus(MemberState.ACTIVE) // 회원가입시 기본값(활동)
                .build();
        memberRepository.save(member);
    }
}
