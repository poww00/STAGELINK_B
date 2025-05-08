package com.pro.user;

import com.pro.dto.MemberRegisterDto;
import com.pro.entity.Gender;
import com.pro.entity.Member;
import com.pro.repository.MemberRepository;
import com.pro.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("모든 필수 정보를 입력하면 회원가입 성공")
    @Test
    public void 회원가입() throws Exception {
        //Given 회원가입 값 입력
        MemberRegisterDto dto = MemberRegisterDto.builder()
                .userId("test")
                .password("abcd1234")
                .confirmPassword("abcd1234")
                .name("김가가")
                .birthday((LocalDate.of(2000,1,1)) )
                .gender(Gender.FEMALE)
                .nickname("가가")
                .userEmail("abc123@naver.com")
                .build();
        //When 회원가입 메서드 실행
        memberService.register(dto);
        //Then 저장된 회원 정보 검증
        Member saved = memberRepository.findByUserId("test");
        assertEquals("test", saved.getUserId());
        assertTrue(passwordEncoder.matches("abcd1234", saved.getPassword()));
        assertEquals("김가가", saved.getName());
        assertEquals(LocalDate.of(2000,1,1), saved.getBirthday());
        assertEquals(Gender.FEMALE, saved.getGender());
        assertEquals("가가", saved.getNickname());
        assertEquals("abc123@naver.com", saved.getUserEmail());
    }

    @DisplayName("중복된 아이디로 회원가입시 예외가 발생")
    @Test
    public void 중복_아이디_예외() {
        // Given
        MemberRegisterDto firstDto = MemberRegisterDto.builder()
                .userId("test")
                .password("abcd1234")
                .confirmPassword("abcd1234")
                .name("김나나")
                .birthday((LocalDate.of(2000,1,1)) )
                .gender(Gender.FEMALE)
                .nickname("나나")
                .userEmail("abc123@naver.com")
                .build();

        MemberRegisterDto secondDto = MemberRegisterDto.builder()
                .userId("test")
                .password("abcd12345")
                .confirmPassword("abcd12345")
                .name("김다다")
                .birthday((LocalDate.of(2001,1,1)) )
                .gender(Gender.MALE)
                .nickname("다다")
                .userEmail("efg123@naver.com")
                .build();
        // When
        memberService.register(firstDto);

        // Then 예외가 발생하지 않으면 테스트 실패
        assertThrows(IllegalStateException.class, () -> memberService.register(secondDto));
    }

    @DisplayName("중복된 이메일로 회원가입시 예외가 발생")
    @Test
    public void 중복_이메일_예외() {
        // Given
        MemberRegisterDto firstDto = MemberRegisterDto.builder()
                .userId("test1")
                .password("abcd1234")
                .confirmPassword("abcd1234")
                .name("김나나")
                .birthday((LocalDate.of(2000,1,1)) )
                .gender(Gender.FEMALE)
                .nickname("나나")
                .userEmail("abc123@naver.com")
                .build();

        MemberRegisterDto secondDto = MemberRegisterDto.builder()
                .userId("test2")
                .password("abcd12345")
                .confirmPassword("abcd12345")
                .name("김다다")
                .birthday((LocalDate.of(2001,1,1)) )
                .gender(Gender.MALE)
                .nickname("다다")
                .userEmail("abc123@naver.com")
                .build();
        // When
        memberService.register(firstDto);

        // Then 예외가 발생하지 않으면 테스트 실패
        assertThrows(IllegalStateException.class, () -> memberService.register(secondDto));
    }
}
