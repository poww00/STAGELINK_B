package com.pro.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pro.entity.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberUpdateDto {

    private String userEmail;
    private String name;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Gender gender;
}
