package com.pro.service.Impl;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;
import com.pro.repository.ReservationRepository;
import com.pro.service.RankingService;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final ReservationRepository reservationRepository;
    private final ShowInfoService       showInfoService;   // 공개 API만 사용

    /* ───────────── 종합 TOP 20 ───────────── */
    @Override
    public List<ShowInfoDTO> getTop20Shows() {
        return mapToDTO(reservationRepository.findTop20Shows());
    }

    /* ───────────── 성별 TOP 20 ───────────── */
    @Override
    public List<ShowInfoDTO> getTop20ShowsByGender(Gender gender) {
        return mapToDTO(
                reservationRepository.findTop20ShowsByGender(gender.name())
        );
    }

    /* ──────────── 연령대 TOP 20 ──────────── */
    @Override
    public List<ShowInfoDTO> getTop20ShowsByAge(String ageGroup) {
        int currentYear = 2025;     // 기준 연도

        int startYear;              // 이상
        int endYear;                // 이하

        switch (ageGroup) {
            case "10대" -> { startYear = currentYear - 19; endYear = currentYear - 10; }
            case "20대" -> { startYear = currentYear - 29; endYear = currentYear - 20; }
            case "30대" -> { startYear = currentYear - 39; endYear = currentYear - 30; }
            case "40대" -> { startYear = currentYear - 49; endYear = currentYear - 40; }
            case "50대" -> { startYear = currentYear - 59; endYear = currentYear - 50; }
            case "60대 이상" -> { startYear = 1900;          endYear = currentYear - 60; }
            default -> throw new IllegalArgumentException("잘못된 연령대: " + ageGroup);
        }

        return mapToDTO(
                reservationRepository.findTop20ShowsByAgeRange(startYear, endYear)
        );
    }

    /* ──────────── 공통 변환 로직 ──────────── */
    private List<ShowInfoDTO> mapToDTO(List<Object[]> rows) {
        List<ShowInfoDTO> list = new ArrayList<>();
        for (Object[] row : rows) {
            Integer showInfoId = ((Number) row[0]).intValue();   // show_info_id
            try {
                list.add(showInfoService.getShowInfo(showInfoId));
            } catch (Exception ignored) { /* 삭제된 공연 무시 */ }
        }
        return list;
    }
}
