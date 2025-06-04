package com.pro.dto;

import com.pro.entity.Show;
import com.pro.entity.ShowInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShowInfoDTO {

    // 기본 정보
    private Integer id;             // 공연 ID
    private String name;           // 공연 제목
    private String explain;        // 공연 설명
    private String poster;         // 포스터 이미지 URL
    private String category;       // 공연 장르
    private Integer age;           // 관람 가능 연령
    private String duration;       // 공연 소요 시간
    private Integer locationId;    // 공연장 ID (FK)

    // 공연 상세 이미지
    private String styUrl1;        // 공연 이미지 1
    private String styUrl2;        // 공연 이미지 2
    private String styUrl3;        // 공연 이미지 3
    private String styUrl4;        // 공연 이미지 4

    // 장소명/주소, 공연기간
    private String locationName;     // 공연장 이름
    private String locationAddress;  // 공연장 주소
    private String periodStart;      // 공연 시작일 (yyyy-MM-dd)
    private String periodEnd;        // 공연 종료일 (yyyy-MM-dd)

    // 대표 가격
    private Integer seatA;         // A석 가격
    private Integer seatR;         // R석 가격
    private Integer seatS;         // S석 가격
    private Integer seatVIP;       // VIP석 가격

    // 후기 별점 평균
    private Double rating;         // 별점 평균

    // 대표 회차 정보 (가장 가까운 회차 기준)
    private Long nearestShowId;         // 대표 회차 ID
    private LocalDateTime nearestShowStart;  // 시작 시간
    private Integer nearestShowState;        // 회차 상태 (예: 0:예약 가능, 1:매진, 등)
    private Integer nearestShowRPrice;       // R석 가격 (회차 기준)
    private Integer nearestShowSPrice;       // S석 가격 (회차 기준)
    private Integer nearestShowAPrice;       // A석 가격 (회차 기준)
    private Integer nearestShowVipPrice;     // VIP석 가격 (회차 기준)

    /**
     * ShowInfo + 대표 회차 + 장소명 + 가격 + 별점 포함하여 DTO 변환
     */
    public static ShowInfoDTO fromEntity(
            ShowInfo showInfo,
            String locationName,
            String locationAddress,
            String periodStart,
            String periodEnd,
            Integer seatA,
            Integer seatR,
            Integer seatS,
            Integer seatVIP,
            Double rating,
            Show nearestShow
    ) {
        return ShowInfoDTO.builder()
                .id(showInfo.getId())
                .name(showInfo.getName())
                .explain(showInfo.getExplain())
                .poster(showInfo.getPoster())
                .category(showInfo.getCategory())
                .age(showInfo.getAge())
                .duration(showInfo.getDuration())
                .locationId(showInfo.getLocationId())
                .styUrl1(showInfo.getStyUrl1())
                .styUrl2(showInfo.getStyUrl2())
                .styUrl3(showInfo.getStyUrl3())
                .styUrl4(showInfo.getStyUrl4())
                .locationName(locationName)
                .locationAddress(locationAddress)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .seatA(seatA)
                .seatR(seatR)
                .seatS(seatS)
                .seatVIP(seatVIP)
                .rating(rating)
                .nearestShowId(nearestShow != null ? nearestShow.getShowNo() : null)
                .nearestShowStart(nearestShow != null ? nearestShow.getShowStartTime() : null)
                .nearestShowState(nearestShow != null ? nearestShow.getShowState() : null)
                .nearestShowRPrice(nearestShow != null ? nearestShow.getSeatRPrice() : null)
                .nearestShowSPrice(nearestShow != null ? nearestShow.getSeatSPrice() : null)
                .nearestShowAPrice(nearestShow != null ? nearestShow.getSeatAPrice() : null)
                .nearestShowVipPrice(nearestShow != null ? nearestShow.getSeatVipPrice() : null)
                .build();
    }
}
