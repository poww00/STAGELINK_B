package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 배우 - 공연 출연 정보 엔티티
 * TBL_ACTOR_SHOW 테이블과 매핑되며, 배우가 어떤 공연에 어떤 배역으로 출연했는지 저장한다.
 * 복합키(배우번호 + 공연정보번호)를 사용하며, 출연 기간과 배역 정보도 포함됨.
 */
@Entity
@Table(name = "TBL_ACTOR_SHOW")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorShow {

    @EmbeddedId
    private ActorShowId id; // 복합키 클래스 (배우 번호 + 공연 번호)

    @MapsId("actorNo") // 복합키의 actorNo 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTOR_NO")
    private Actor actor; // 참조 배우 (TBL_ACTOR FK)

    @MapsId("showInfo") // 복합키의 showInfo 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SHOW_INFO")
    private ShowInfo showInfo; // 참조 공연 정보 (TBL_SHOWINFO FK)

    @Column(name = "ROLE_NAME", length = 50)
    private String roleName; // 배역 이름 (예: '햄릿', '엘파바')

    @Column(name = "SHOW_START_TIME")
    private LocalDate showStartTime; // 배우가 공연에 참여한 시작일

    @Column(name = "SHOW_END_TIME")
    private LocalDate showEndTime; // 배우가 공연에 참여한 종료일
}
