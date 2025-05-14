package com.pro.service.Impl;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;
import com.pro.repository.ReservationRepository;
import com.pro.repository.ShowInfoRepository;
import com.pro.service.RankingService;
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
    private final ShowInfoRepository showInfoRepository;

    @Override
    public List<ShowInfoDTO> getTop20Shows() {
        return mapToDTO(reservationRepository.findTop20Shows());
    }

    @Override
    public List<ShowInfoDTO> getTop20ShowsByGender(Gender gender) {
        // gender.name() 사용해서 String 값으로 넘긴다
        List<Object[]> result = reservationRepository.findTop20ShowsByGender(gender.name());
        return mapToDTO(result);
    }

    @Override
    public Map<String, List<ShowInfoDTO>> getTop20ShowsByAgeGroups() {
        Map<String, List<ShowInfoDTO>> rankings = new LinkedHashMap<>();
        int currentYear = 2025;

        rankings.put("10대", getByAgeRange(currentYear - 19, currentYear - 10));
        rankings.put("20대", getByAgeRange(currentYear - 29, currentYear - 20));
        rankings.put("30대", getByAgeRange(currentYear - 39, currentYear - 30));
        rankings.put("40대", getByAgeRange(currentYear - 49, currentYear - 40));
        rankings.put("50대", getByAgeRange(currentYear - 59, currentYear - 50));
        rankings.put("60대 이상", getByAgeRange(1900, currentYear - 60));

        return rankings;
    }

    private List<ShowInfoDTO> getByAgeRange(int startYear, int endYear) {
        return mapToDTO(reservationRepository.findTop20ShowsByAgeRange(startYear, endYear));
    }

    private List<ShowInfoDTO> mapToDTO(List<Object[]> results) {
        List<ShowInfoDTO> dtoList = new ArrayList<>();
        for (Object[] result : results) {
            Integer showNo = ((Number) result[0]).intValue();
            showInfoRepository.findById(showNo).ifPresent(showInfo ->
                    dtoList.add(ShowInfoDTO.fromEntity(showInfo))
            );
        }
        return dtoList;
    }
}
