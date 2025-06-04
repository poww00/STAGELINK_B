package com.pro.service.Impl;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;
import com.pro.repository.ReservationRepository;
import com.pro.service.RankingService;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final ReservationRepository reservationRepository;
    private final ShowInfoService showInfoService;   // 공개 API만 사용

    /*  TOP20 종합  */
    @Override
    public List<ShowInfoDTO> getTop20Shows() {
        return mapToDTO(reservationRepository.findTop20Shows());
    }

    /* 성별 TOP20  */
    @Override
    public List<ShowInfoDTO> getTop20ShowsByGender(Gender gender) {
        return mapToDTO(reservationRepository.findTop20ShowsByGender(gender.name()));
    }

    /*  연령대 TOP20  */
    @Override
    public Map<String, List<ShowInfoDTO>> getTop20ShowsByAgeGroups() {
        Map<String, List<ShowInfoDTO>> rankings = new LinkedHashMap<>();
        int currentYear = 2025;   // 기준연도

        rankings.put("10대", getByAgeRange(currentYear - 19, currentYear - 10));
        rankings.put("20대", getByAgeRange(currentYear - 29, currentYear - 20));
        rankings.put("30대", getByAgeRange(currentYear - 39, currentYear - 30));
        rankings.put("40대", getByAgeRange(currentYear - 49, currentYear - 40));
        rankings.put("50대", getByAgeRange(currentYear - 59, currentYear - 50));
        rankings.put("60대 이상", getByAgeRange(1900, currentYear - 60));

        return rankings;
    }

    /*  내부 헬퍼  */
    private List<ShowInfoDTO> getByAgeRange(int startYear, int endYear) {
        return mapToDTO(reservationRepository.findTop20ShowsByAgeRange(startYear, endYear));
    }

    /**
     * ReservationRepository의 쿼리 결과(Object[] 목록)를 ShowInfoDTO 목록으로 변환
     * - row[0]: showInfo PK (Integer 또는 Long)
     * - 삭제된 공연 예외 대비 try/catch 처리
     */
    private List<ShowInfoDTO> mapToDTO(List<Object[]> results) {
        List<ShowInfoDTO> dtoList = new ArrayList<>();
        for (Object[] row : results) {
            Integer showInfoId = ((Number) row[0]).intValue();
            try {
                dtoList.add(showInfoService.getShowInfo(showInfoId));
            } catch (Exception ignored) {
                // 삭제된 공연이거나 조회 실패한 경우 무시
            }
        }
        return dtoList;
    }
}
