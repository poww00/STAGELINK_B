package com.pro.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * 배우-공연 매핑 테이블의 복합 키 클래스
 * TBL_ACTOR_SHOW 테이블의 PK로 사용되는 복합키 정의
 * actorNo + showInfo 조합으로 출연 정보를 유일하게 식별
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 복합키 비교를 위해 반드시 필요
@Embeddable // JPA에서 복합키로 사용할 수 있도록 지정
public class ActorShowId implements Serializable {

    @Column(name = "ACTOR_NO")
    private Integer actorNo; // 배우 고유 번호 (TBL_ACTOR의 FK)

    @Column(name = "SHOW_INFO")
    private Integer showInfo; // 공연 정보 고유 번호 (TBL_SHOWINFO의 FK)
}

