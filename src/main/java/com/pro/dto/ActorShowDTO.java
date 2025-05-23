package com.pro.dto;

import com.pro.entity.ActorShow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 배우 출연작 정보 DTO
 * - 공연 정보 및 배우의 배역을 포함하여
 *   배우 상세 페이지, 공연 상세 페이지에서 활용됨
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorShowDTO {

    private Integer showInfoId;   // 공연 정보 번호 (TBL_SHOWINFO의 PK)
    private String title;         // 공연 제목
    private Integer locationId;   // 공연 장소 ID
    private String startDate;     // 출연 시작일 (yyyy-MM-dd)
    private String endDate;       // 출연 종료일 (yyyy-MM-dd)
    private String poster;        // 공연 포스터 이미지 URL
    private String roleName;      // 배역 이름 (예: 엘파바, 햄릿)

    /**
     * ActorShow 엔티티 → DTO 변환 메서드
     * 공연 정보와 배역명을 추출하여 DTO로 변환
     */
    public static ActorShowDTO fromEntity(ActorShow actorShow) {
        return ActorShowDTO.builder()
                .showInfoId(actorShow.getShowInfo().getId())
                .title(actorShow.getShowInfo().getName())
                .locationId(actorShow.getShowInfo().getLocationId())
                .startDate(actorShow.getShowStartTime().toString())
                .endDate(actorShow.getShowEndTime().toString())
                .poster(actorShow.getShowInfo().getPoster())
                .roleName(actorShow.getRoleName())
                .build();
    }
}
