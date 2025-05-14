package com.pro.service;

import com.pro.dto.ShowInfoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShowInfoService {

    // 공연 전체 조회
    List<ShowInfoDTO> getAllShowInfos();

    // 공연 상세 조회
    ShowInfoDTO getShowInfo(Integer id);

    // 정렬 + 페이징 조회
    Page<ShowInfoDTO> getSortedShowInfoList(String sortBy, String direction, int page, int size, boolean excludeEnded);

    // 공연 검색
    Page<ShowInfoDTO> searchShowInfos(String keyword, int page, int size);

    // 카테고리 필터링
    Page<ShowInfoDTO> filterByCategory(String category, int page, int size);
}
