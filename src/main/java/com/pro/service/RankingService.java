package com.pro.service;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.Gender;

import java.util.List;
import java.util.Map;

public interface RankingService {

    List<ShowInfoDTO> getTop20Shows();

    List<ShowInfoDTO> getTop20ShowsByGender(Gender gender);

    Map<String, List<ShowInfoDTO>> getTop20ShowsByAgeGroups();
}
