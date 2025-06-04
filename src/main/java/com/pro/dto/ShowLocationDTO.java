package com.pro.dto;

import com.pro.entity.ShowLocation;
import lombok.*;

/**
 * ShowLocationDTO
 * 공연장(위치) 정보를 전달하는 DTO 클래스
 * 공연 상세 페이지나 예매 페이지 등에서 공연장 이름과 주소를 출력할 때 사용됨
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLocationDTO {

    private Long id;        // 공연장 ID (PK)

    private String name;    // 공연장 이름

    private String address; // 공연장 주소

    /**
     * ShowLocation 엔티티 → DTO 변환 메서드
     * @param entity ShowLocation 엔티티
     * @return ShowLocationDTO 객체
     */
    public static ShowLocationDTO fromEntity(ShowLocation entity) {
        return ShowLocationDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .build();
    }
}
