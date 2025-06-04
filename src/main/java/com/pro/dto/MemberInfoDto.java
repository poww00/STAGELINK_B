package com.pro.dto;

import com.pro.entity.Gender;
import com.pro.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfoDto {
    private String userId;
    private String userEmail;
    private String name;
    private String nickname;
    private LocalDate birthday;
    private Gender gender;

    public static MemberInfoDto from(Member member) {

        LocalDate birthday = member.getBirthday() != null ? member.getBirthday() : null;
        Gender gender = member.getGender() != null ? member.getGender() : null;

        return MemberInfoDto.builder()
                .userId(member.getUserId())
                .userEmail(member.getUserEmail())
                .name(member.getName())
                .nickname(member.getNickname())
                .birthday(birthday)
                .gender(gender)
                .build();
    }
}
