package com.pro.service;

import com.pro.dto.ShowDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * ShowService
 * 공연 회차(Show) 관련 서비스 인터페이스
 */
public interface ShowService {

    // 공연 회차 번호로 단일 회차 정보 조회
    ShowDTO getShow(Integer showNo);

    //모든 공연 회차 전체 조회
    List<ShowDTO> getAllShows();

    //상태값 리스트(예: 예약 가능/예정 등) 기준 회차 목록 조회 (페이지네이션 포함)
    Page<ShowDTO> getShowsByStates(List<Integer> states, int page, int size);

    // 공연 정보 ID 기준 회차 목록 조회 (종료된 회차 제외)
    List<ShowDTO> getShowsByShowInfo(Integer showInfoId);
}
