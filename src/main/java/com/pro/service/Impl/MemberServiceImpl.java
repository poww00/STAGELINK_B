package com.pro.service.Impl;

import com.pro.dto.MemberRegisterDto;
import com.pro.dto.ResetPasswordDto;
import com.pro.entity.Gender;
import com.pro.entity.Member;
import com.pro.entity.MemberState;
import com.pro.repository.MemberRepository;
import com.pro.security.JwtTokenProvider;
import com.pro.security.jwt.JwtTokenDto;
import com.pro.service.MemberService;
import com.pro.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

    // 회원가입
    @Override
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
                .gender(dto.getGender())
                .nickname(dto.getNickname())
                .userEmail(dto.getUserEmail())
                .memberStatus(MemberState.ACTIVE) // 회원가입시 기본값(활동)
                .build();
        memberRepository.save(member);
    }

    // db에서 회원 조회, 없다면 예외 던짐
    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
    }

    // 로그인
    @Override
    public Member login(String userId, String password) {
        // 유저 조회
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음: " + userId));
        // 비밀번호 검증
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        JwtTokenDto tokenDto = jwtTokenProvider.generateToken(member); // 토큰 생성

        refreshTokenService.save(userId, tokenDto.getRefreshToken()); // refresh토큰 저장
        return member;
    }

    // 아이디 찾기
    @Override
    public String findUserIdByNameAndEmail(String name, String userEmail) {
        return memberRepository.findByNameAndUserEmail(name, userEmail)
                .map(Member::getUserId)
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 회원 정보가 없습니다."));
    }

    // 비밀번호 찾기 - 사용자 확인
    @Override
    public void verifyUserByIdAndEmail(String userId, String userEmail) {
        boolean exists = memberRepository.findByUserId(userId)
                .filter(m -> m.getUserEmail().equals(userEmail))
                .isPresent();

        if (!exists) {
            throw new IllegalStateException("일치하는 회원이 없습니다.");
        }
    }

    // 비밀번호 찾기 - 재설정
    @Override
    public void resetPassword(ResetPasswordDto dto) {

        // 비밀번호 일치 확인
        if (!dto.isPasswordMatching()) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Member member = memberRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
        member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        memberRepository.save(member);
    }


    // 카카오 로그인
    @Override
    public Map<String, Object> getKakaoMember(String code) {
        String accessToken = getAccessTokenFromKakao(code);
        log.info("accessToken: {}", accessToken); // 토큰 확인
        String kakaoUserInfoUrl = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity<String> request = new HttpEntity<>(headers);

        // 사용자 정보 요청
        ResponseEntity<Map> response = restTemplate.exchange(
                kakaoUserInfoUrl,
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");

        String email = (String) kakaoAccount.get("email");
        String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");

        // 이메일로 기존 회원 조회
        Member member = memberRepository.findByUserEmail(email).orElseGet(() ->
                // 없다면 소셜 회원 자동 가입
                registerSocialMember("kakao_", email, null, nickname, null, null)
        );

        return generateLoginResponse(member);
    }

    // 네이버 로그인 - 잠시 중단..
    @Override
    public Map<String, Object> getNaverMember(String code, String state) {
        String accessToken = getAccessTokenFromNaver(code, state);
        String naverUserInfoUrl = "https://openapi.naver.com/v1/nid/me";

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        // 사용자 정보 요청
        ResponseEntity<Map> response = restTemplate.exchange(
                naverUserInfoUrl,
                HttpMethod.GET,
                request,
                Map.class
        );

        // 응답 파싱
        Map<String, Object> body = response.getBody();
        Map<String, Object> naverAccount = (Map<String, Object>) body.get("response");

        String email = (String) naverAccount.get("email");
        String nickname = (String) naverAccount.get("nickname");
        String name = (String) naverAccount.get("name");
        String birthyear = (String) naverAccount.get("birthyear"); // 연도 "2000"
        String birthdayStr = (String) naverAccount.get("birthday"); // 생일 "05-13"
        String genderStr = (String) naverAccount.get("gender"); // "M" or "F"

        LocalDate birthday = parseBirthday(birthyear, birthdayStr); // 연도 + 생일
        Gender gender = parseGender(genderStr); // enum 처리

        // 이메일로 회원 조회
        Member member = memberRepository.findByUserEmail(email).orElseGet(() ->
                //없다면 회원가입
                registerSocialMember("naver_", email, name, nickname, birthday, gender)
        );

        return generateLoginResponse(member);
    }

    //카카오로부터 access token을 발급받는 메서드
    private String getAccessTokenFromKakao(String code) {
        // 카카오 토큰 요청 URL
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문 파라미터 구성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoClientId); //REST_API 키
        params.add("redirect_uri", kakaoRedirectUri); //인가코드 리다이렉트 주소
        params.add("code", code);
        // 해더 + 파라미터를 포함한 요청 객체 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        // 토큰 발급 요청
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        log.info("인가 코드: {}", code);
        log.info("요청 redirect_uri: {}", "http://localhost:3000/member/kakao");


        return (String) response.getBody().get("access_token");
    }

    // 네이버로부터 access token 발급 받는 메서드
    private String getAccessTokenFromNaver(String code, String state) {
        // 네이버 토큰 요청
        String tokenUri = "https://nid.naver.com/oauth2.0/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 요청 본문 파라미터 구성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", ""); // 네이버 클라이언트 ID
        params.add("client_secret",""); // 네이버 시크릿 키
        params.add("redirect_uri", "http://localhost:3000/member/naver");
        params.add("code", code);
        params.add("state", state); // 네이버는 state 필수

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, request, Map.class);

        log.info("인가 코드: {}", code);
        log.info("요청 redirect_uri: {}", "http://localhost:3000/member/naver");

        return (String) response.getBody().get("access_token");
    }


    // 소셜 회원가입 메서드
    private Member registerSocialMember(String prefix, String email, String name, String nickname, LocalDate birthday, Gender gender) {
        String tempId = prefix + UUID.randomUUID().toString().substring(0, 8);
        String tempPw = UUID.randomUUID().toString();

        Member newMember = Member.builder()
                .userId(tempId)
                .password(passwordEncoder.encode(tempPw))
                .nickname(nickname)
                .userEmail(email)
                .name(name)
                .birthday(birthday)
                .gender(gender)
                .memberStatus(MemberState.ACTIVE)
                .build();

        return memberRepository.save(newMember);
    }

    // 회원정보를 기반으로 JWT 발급 및 로그인 응답 Map 구성
    private Map<String, Object> generateLoginResponse(Member member) {
        // JWT 발급
        JwtTokenDto tokenDto = jwtTokenProvider.generateToken(member);
        refreshTokenService.save(member.getUserId(), tokenDto.getRefreshToken());

        // 응답 데이터 구성
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", tokenDto.getAccessToken());
        result.put("refreshToken", tokenDto.getRefreshToken());
        result.put("nickname", member.getNickname());
        result.put("email", member.getUserEmail());

        return result;
    }


    // 생년월일 조합
    private LocalDate parseBirthday(String birthyear, String birthdayStr) {
        if (birthyear == null || birthdayStr == null || birthyear.isBlank() || birthdayStr.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(birthyear + "-" + birthdayStr);
        } catch (DateTimeParseException e) {
            log.warn("잘못된 생일 형식: {}-{}", birthyear, birthdayStr);
            return null;
        }
    }

    // 성별 string -> enum 변경
    private Gender parseGender(String genderStr) {
        if (genderStr == null || genderStr.isBlank()) return null;
        if (genderStr.equalsIgnoreCase("M")) return Gender.MALE;
        if (genderStr.equalsIgnoreCase("F")) return Gender.FEMALE;
        return null;
    }
}
