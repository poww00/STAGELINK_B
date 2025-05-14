package com.pro.service;

import com.pro.dto.ShowDTO;

import java.util.List;

public interface ShowService {

    // 공연 전체 조회
    List<ShowDTO> getAllShows();

    // 공연 상세 조회
    ShowDTO getShow(Integer showNo);

    List<ShowDTO> getAvailableShows(); // [추가] 예매 가능한 공연만 조회
}
