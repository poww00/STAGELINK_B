package com.pro.service;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;

import java.util.List;

/** 공연 랭킹(판매량 기준 TOP20) 서비스 */
public interface RankingService {

    /** 전체 예매 수 기준 TOP 20 */
    List<ShowInfoDTO> getTop20Shows();

    /** 성별 기준 TOP 20 */
    List<ShowInfoDTO> getTop20ShowsByGender(Gender gender);

    /** 연령대 기준 TOP 20 (10대, 20대 … 60대 이상) */
    List<ShowInfoDTO> getTop20ShowsByAge(String ageGroup);
}
