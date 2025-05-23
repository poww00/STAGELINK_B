package com.pro.controller.member;

import com.pro.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/member")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class SocialController {
    private final MemberService memberService;

    @GetMapping("/kakao")
    public Map<String, Object> getMemberFromKakao(@RequestParam("code") String code) {
        System.out.println("인가 코드: " + code);

        return memberService.getKakaoMember(code);
    }

    @GetMapping("/naver")
    public Map<String, Object> getMemberFromNaver(@RequestParam("code") String code,
                                                  @RequestParam("state") String state) {
        log.info("accessToken: {}", code);

        return memberService.getNaverMember(code,state);
    }

}