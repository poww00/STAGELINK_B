package com.pro.controller.member;

import com.pro.dto.MemberRegisterDto;
import com.pro.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody MemberRegisterDto dto) {
        memberService.register(dto);
        return ResponseEntity.ok("회원가입 성공!");
    }
}
