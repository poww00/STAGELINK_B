package com.pro.service;

import com.pro.controller.ShowInfoController.CastDTO;
import com.pro.controller.ShowInfoController.ReviewPreviewDTO;
import com.pro.dto.ShowInfoDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * ShowInfoService
 * 공연 정보(ShowInfo) 관련 조회 서비스 인터페이스
 */
public interface ShowInfoService {

    /** 모든 공연 정보 조회 */
    List<ShowInfoDTO> getAllShowInfos();

    /** 공연 ID로 상세 정보 조회 */
    ShowInfoDTO getShowInfo(Integer id);

    /** 공연 정렬 목록 조회 (이름, 날짜, 카테고리 등) */
    Page<ShowInfoDTO> getSortedShowInfoList(String sortBy, String direction,
                                            int page, int size, boolean excludeEnded);

    /** 공연명 키워드 검색 (회차 존재 공연만) */
    Page<ShowInfoDTO> searchShowInfos(String keyword, int page, int size);

    /** 카테고리별 공연 필터링 */
    Page<ShowInfoDTO> filterByCategory(String category, int page, int size);

    /** 출연 배우 목록 조회 */
    List<CastDTO> getCasts(Integer showInfoId);

    /** 후기 미리보기 조회 */
    List<ReviewPreviewDTO> getReviews(Integer showInfoId, int size);
}
