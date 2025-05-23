package com.pro.dto;

import com.pro.entity.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Setter
@Getter
public class MemberRegisterDto {
    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(
            regexp = "^[A-Za-z0-9]{8,20}$",
            message = "아이디는 영문자와 숫자를 포함한 8~20자여야 합니다."
    )
    private String userId;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 영문자와 숫자를 포함한 8~20자여야 합니다."
    )
    private String password;
    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;
    @NotBlank(message = "이름은 필수입니다.")
    private String name;
    @NotNull(message = "생년월일은 필수입니다.")
    private LocalDate birthday;
    /*    private String postNo;
        private String address;*/
    @NotNull(message = "성별을 선택해주세요.")
    private Gender gender;
    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;
    @NotBlank(message = "이메일은 필수입니다")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다."
    )
    private String userEmail;
}