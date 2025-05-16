package com.pro.service;

import com.pro.dto.ShowDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShowService {

    // 공연 전체 조회
    List<ShowDTO> getAllShows();

    // 공연 상세 조회
    ShowDTO getShow(Integer showNo);

    // 특정 상태값을 기준으로 공연 조회
    Page<ShowDTO> getShowsByStates(List<Integer> states, int page, int size);
}
