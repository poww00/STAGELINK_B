package com.pro.service;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;

import java.util.List;
import java.util.Map;

/**
 * RankingService
 * 공연 랭킹(판매량 기준 TOP20) 관련 서비스 인터페이스
 */
public interface RankingService {

    //전체 예매 수 기준 TOP 20 공연 조회
    List<ShowInfoDTO> getTop20Shows();

    //성별 기준 TOP 20 공연 조회
    List<ShowInfoDTO> getTop20ShowsByGender(Gender gender);

    // 연령대별 TOP 20 공연 조회
    Map<String, List<ShowInfoDTO>> getTop20ShowsByAgeGroups();
}
