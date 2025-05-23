package com.pro.dto;

import com.pro.entity.Actor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 배우 상세 정보를 담는 DTO 클래스
 * - 배우 기본 정보 (번호, 이름, 이미지, 프로필)
 * - 출연작 목록 (ActorShowDTO 리스트 형태로 포함)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {

    private Integer actorNo;       // 배우 번호 (PK)
    private String actorName;      // 배우 이름
    private String actorImage;     // 배우 프로필 이미지 URL
    private String actorProfile;   // 배우 프로필 텍스트 (출생지, 학력, 수상 내역 등)
    private List<ActorShowDTO> shows; // 출연작 리스트

    /**
     * Entity → DTO 변환 메서드
     * Actor 엔티티와 출연작 DTO 리스트를 조합해 ActorDTO 생성
     */
    public static ActorDTO fromEntity(Actor actor, List<ActorShowDTO> shows) {
        return ActorDTO.builder()
                .actorNo(actor.getActorNo())
                .actorName(actor.getActorName())
                .actorImage(actor.getActorImage())
                .actorProfile(actor.getActorProfile())
                .shows(shows)
                .build();
    }
}
