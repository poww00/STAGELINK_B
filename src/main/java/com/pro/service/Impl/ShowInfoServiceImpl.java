package com.pro.service.Impl;

import com.pro.dto.ShowInfoDTO;
import com.pro.entity.ShowInfo;
import com.pro.repository.ShowInfoRepository;
import com.pro.service.ShowInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowInfoServiceImpl implements ShowInfoService {

    private final ShowInfoRepository showInfoRepository;

    // 전체 공연 기본 정보 목록 조회
    @Override
    public List<ShowInfoDTO> getAllShowInfos() {
        return showInfoRepository.findAll()
                .stream()
                .map(ShowInfoDTO::fromEntity) // 엔티티 → DTO 변환
                .collect(Collectors.toList());
    }

    // ID를 통한 공연 기본 정보 상세 조회
    @Override
    public ShowInfoDTO getShowInfo(Integer id) {
        ShowInfo showInfo = showInfoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공연 정보가 존재하지 않습니다."));
        return ShowInfoDTO.fromEntity(showInfo);
    }

    // 공연 기본 정보 정렬 + 페이징 조회 (옵션: 종료 공연 제외)
    @Override
    public Page<ShowInfoDTO> getSortedShowInfoList(String sortBy, String direction, int page, int size, boolean excludeEnded) {
        // 정렬 방향 설정
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ShowInfo> showInfos;

        // 공연 상태가 '종료(5)'가 아닌 것만 조회
        if (excludeEnded) {
            showInfos = showInfoRepository.findByShowsShowStateNot(5, pageable);
        } else {
            showInfos = showInfoRepository.findAll(pageable);
        }

        return showInfos.map(ShowInfoDTO::fromEntity); // Page<ShowInfo> → Page<ShowInfoDTO>
    }

    // 공연 제목을 기준으로 검색 (페이징 포함)
    @Override
    public Page<ShowInfoDTO> searchShowInfos(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return showInfoRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(ShowInfoDTO::fromEntity);
    }

    // 카테고리 필터링
    @Override
    public Page<ShowInfoDTO> filterByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return showInfoRepository.findByCategoryIgnoreCase(category, pageable)
                .map(ShowInfoDTO::fromEntity);
    }
}
