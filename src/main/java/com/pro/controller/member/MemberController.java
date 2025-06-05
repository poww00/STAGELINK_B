package com.pro.controller.member;

import com.pro.dto.FindIdRequestDto;
import com.pro.dto.MemberRegisterDto;
import com.pro.dto.ResetPasswordDto;
import com.pro.repository.MemberRepository;
import com.pro.service.MemberService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 회원가입
    @PostMapping("/member/register")
    public ResponseEntity<?> register(@Valid @RequestBody MemberRegisterDto dto) {
        memberService.register(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 아이디 중복확인
    @GetMapping("/member/check-userId")
    public ResponseEntity<?> checkId(@RequestParam String userId) {
        boolean exist = memberRepository.existsByUserId(userId);
        return ResponseEntity.ok(new CheckResponse(!exist));
    }

    // 이메일 중복확인
    @GetMapping("/member/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String userEmail) {
        boolean exist = memberRepository.existsByUserEmail(userEmail);
        return ResponseEntity.ok(new CheckResponse(!exist));
    }

    // 아이디 찾기
    @PostMapping("member/find-id")
    public ResponseEntity<String> findId(@Valid @RequestBody FindIdRequestDto dto) {
        String userId = memberService.findUserIdByNameAndEmail(dto.getName(), dto.getUserEmail());
        return ResponseEntity.ok(userId);
    }

    // 비밀번호 찾기 - 사용자 확인
    @PostMapping("member/find-pw")
    public ResponseEntity<String> findPw(@RequestBody ResetPasswordDto dto) {
        memberService.verifyUserByIdAndEmail(dto.getUserId(), dto.getUserEmail());
        return ResponseEntity.ok("인증 성공");
    }

    // 비밀번호 재설정
    @PostMapping("member/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto dto) {
        memberService.resetPassword(dto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 응답 객체 생성 클래스
    @Getter
    @AllArgsConstructor
    static class CheckResponse {
        private boolean available;
    }
}
