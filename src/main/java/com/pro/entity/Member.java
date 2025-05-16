package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Table(name = "TBL_MEMBER")
@ToString(exclude = "password")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_NO", nullable = false) // 회원번호 (PK)
    private Long id;

    @Column(name = "ID", length = 50, nullable = false, unique = true)
    private String userId;  // 로그인용 ID
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "NAME",length = 10, nullable = false)
    private String name;

    @Column(name = "BIRTHDAY", nullable = false)
    private LocalDate birthday;

    /*   현재 티켓 수령 방식: 현장 수령으로 사용X
    @Column(name = "POST_NO", length = 50)
    private String postNo;
    @Column(name = "ADDRESS", length = 50)
    private String address;
    */

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", nullable = false)
    private Gender gender;
    @Column(name = "NICKNAME", length = 20, nullable = false)
    private String nickname;

    @Column(name = "USER_EMAIL", length = 50, nullable = false, unique = true)
    private String userEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "MEMBER_STATE", nullable = false)
    private MemberState memberStatus;
}
