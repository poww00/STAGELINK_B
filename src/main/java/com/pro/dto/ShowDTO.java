package com.pro.dto;

import com.pro.entity.Show;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ShowDTO {

    // 공연 ID (PK)
    private Integer id;

    // 공연의 기본 정보 ID (TBL_SHOWINFO와 연관)
    private Integer showInfoId;

    // 공연 시작 일시 (회차 시작 시간)
    private LocalDateTime startTime;

    // 공연 종료 일시 (회차 종료 시간)
    private LocalDateTime endTime;

    // R석 가격
    private Integer seatRPrice;

    // S석 가격
    private Integer seatSPrice;

    // A석 가격
    private Integer seatAPrice;

    // VIP석 가격
    private Integer seatVipPrice;

    //R좌석 갯수
    private Integer seatRCount;

    //S좌석 갯수
    private Integer seatSCount;

    //A좌석 갯수
    private Integer seatACount;

    //VIP좌석 갯수
    private Integer seatVipCount;

    // 공연 상태 코드 (0: 판매예정, 1: 판매중, 2: 품절, 3: 예약종료, 4: 진행중, 5: 종료)
    private Integer showState;

    // 공연 제목
    private String showTitle;

    // Show 엔티티를 DTO로 변환하는 정적 메서드
    public static ShowDTO fromEntity(Show show) {
        return ShowDTO.builder()
                .id(show.getShowNo().intValue()) // Long → Integer 변환
                .showInfoId(show.getShowInfo().getId()) // ShowInfo의 id도 Integer로 바뀐 경우
                .startTime(show.getShowStartTime())
                .endTime(show.getShowEndTime())
                .seatRPrice(show.getSeatRPrice())
                .seatAPrice(show.getSeatAPrice())
                .seatSPrice(show.getSeatSPrice())
                .seatVipPrice(show.getSeatVipPrice())
                .seatRCount(show.getSeatRCount())
                .seatSCount(show.getSeatSCount())
                .seatACount(show.getSeatACount())
                .seatVipCount(show.getSeatVipCount())
                .showState(show.getShowState())
                .showTitle(show.getShowInfo().getName())
                .build();
    }
}
