package com.pro.service.Impl;

import com.pro.dto.ShowLocationDTO;
import com.pro.entity.ShowLocation;
import com.pro.repository.ShowLocationRepository;
import com.pro.service.ShowLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * ShowLocationServiceImpl
 * 공연장 정보 조회 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class ShowLocationServiceImpl implements ShowLocationService {

    private final ShowLocationRepository showLocationRepository;

    //공연장 ID로 공연장 정보 조회
    @Override
    public ShowLocationDTO getLocation(Long id) {
        ShowLocation location = showLocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연장이 존재하지 않습니다."));
        return ShowLocationDTO.fromEntity(location);
    }
}
