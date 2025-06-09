package com.pro.dto;

import com.pro.entity.Show;
import com.pro.entity.ShowInfo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShowInfoDTO {

    /* ────────── 기본 정보 ────────── */
    private Integer id;         // 공연 ID
    private String  name;       // 공연 제목
    private String  explain;    // 공연 설명
    private String  poster;     // 포스터 URL
    private String  category;   // 장르
    private Integer age;        // 관람 연령(원본 숫자)
    private String  ageLabel;   // 포맷팅된 연령 ("전체 이용가" 등)
    private String  duration;   // 소요 시간
    private Integer locationId; // 공연장 ID

    /* ────────── 상세 이미지 ────────── */
    private String styUrl1;
    private String styUrl2;
    private String styUrl3;
    private String styUrl4;

    /* ────────── 장소/기간 ────────── */
    private String locationName;
    private String locationAddress;
    private String periodStart; // yyyy-MM-dd
    private String periodEnd;   // yyyy-MM-dd

    /* ────────── 대표 가격 ────────── */
    private Integer seatA;
    private Integer seatR;
    private Integer seatS;
    private Integer seatVIP;

    /* ────────── 후기 별점 평균 ────────── */
    private Double rating;

    /* ────────── 대표 회차 정보 ────────── */
    private Long          nearestShowId;
    private LocalDateTime nearestShowStart;
    private Integer       nearestShowState;
    private Integer       nearestShowRPrice;
    private Integer       nearestShowSPrice;
    private Integer       nearestShowAPrice;
    private Integer       nearestShowVipPrice;

    /* ================================================================
     *  엔티티 + 추가 정보 → DTO 변환
     * ================================================================ */
    public static ShowInfoDTO fromEntity(
            ShowInfo showInfo,
            String   locationName,
            String   locationAddress,
            String   periodStart,
            String   periodEnd,
            Integer  seatA,
            Integer  seatR,
            Integer  seatS,
            Integer  seatVIP,
            Double   rating,
            Show     nearestShow
    ) {
        return ShowInfoDTO.builder()
                .id(showInfo.getId())
                .name(showInfo.getName())
                .explain(showInfo.getExplain())
                .poster(showInfo.getPoster())
                .category(showInfo.getCategory())
                .age(showInfo.getAge())
                .ageLabel(formatAge(showInfo.getAge()))
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

    /* ================================================================
     *  관람 연령 포맷터
     *    - 0         → "전체 이용가"
     *    - 20 이상   → "XX개월 이상"
     *    - 1 ~ 19    → "XX세 이상"
     * ================================================================ */
    private static String formatAge(Integer age) {
        if (age == null) return "";
        if (age == 0)   return "전체 이용가";
        if (age >= 20)  return age + "개월 이상";
        return age + "세 이상";
    }
}
