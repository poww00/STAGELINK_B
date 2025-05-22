package com.pro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordDto {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email
    private String userEmail;

    @NotBlank(message = "새 비밀번호를 입력해주세요")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",
            message = "비밀번호는 영문자와 숫자를 포함한 8~20자여야 합니다."
    )
    private String newPassword; // 사용자가 새로 입력할 비밀번호

    @NotBlank(message = "비밀번호를 한번 더 입력해주세요.")
    private String confirmNewPassword;

    // 비밀번호 일치 확인
    public boolean isPasswordMatching() {
        return newPassword != null && newPassword.equals(confirmNewPassword);
    }

}
