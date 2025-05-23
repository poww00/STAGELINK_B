package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 배우 정보 테이블 매핑 엔티티
 * 해당 클래스는 TBL_ACTOR 테이블과 매핑되며,
 * 배우의 기본 정보를 저장한다.
 */
@Entity
@Table(name = "TBL_ACTOR") // DB 테이블명 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가 (MySQL AUTO_INCREMENT)
    @Column(name = "ACTOR_NO") // DB 컬럼명 명시
    private Integer actorNo; // 배우 고유 번호 (PK)

    @Column(name = "ACTOR_IMAGE", length = 100)
    private String actorImage; // 배우 프로필 이미지 URL

    @Column(name = "ACTOR_NAME", length = 10)
    private String actorName; // 배우 이름

    @Column(name = "ACTOR_PROFILE", length = 1000)
    private String actorProfile; // 배우 소개 (출생지, 학력, 수상 등 프로필 텍스트)
}

