package com.pro.dto;

import com.pro.entity.ShowInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShowInfoDTO {

    // 공연 정보 고유 번호
    private Long id;

    // 공연 제목
    private String name;

    //공연 설명
    private String explain;

    // 공연 포스터 이미지 URL
    private String poster;

    // 공연 장르
    private String category;

    // 관람 가능 연령
    private Integer age;

    // 공연 러닝타임
    private String duration;

    // 공연 장소 ID
    private Integer locationId;

    // 공연 스타일 이미지1
    private String styUrl1;

    // 공연 스타일 이미지2
    private String styUrl2;

    // 공연 스타일 이미지3
    private String styUrl3;

    // 공연 스타일 이미지4
    private String styUrl4;

    // 엔티티 → DTO로 변환하는 정적 메서드
    public static ShowInfoDTO fromEntity(ShowInfo showInfo) {
        return ShowInfoDTO.builder()
                .id(showInfo.getId())
                .poster(showInfo.getPoster())
                .name(showInfo.getName())
                .explain(showInfo.getExplain())
                .category(showInfo.getCategory())
                .age(showInfo.getAge())
                .duration(showInfo.getDuration())
                .locationId(showInfo.getLocationId())
                .styUrl1(showInfo.getStyUrl1())
                .styUrl2(showInfo.getStyUrl2())
                .styUrl3(showInfo.getStyUrl3())
                .styUrl4(showInfo.getStyUrl4())
                .build();
    }
}
