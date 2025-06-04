package com.pro.service.Impl;

import com.pro.dto.ShowDTO;
import com.pro.entity.Show;
import com.pro.repository.ShowRepository;
import com.pro.service.ShowService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ShowServiceImpl
 * 공연 회차(Show) 관련 서비스 구현체
 * 개별 회차 조회, 전체 조회, 상태 필터링, 공연 정보별 회차 조회 기능 제공
 */
@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    public ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    //공연 회차 ID로 단일 회차 정보 조회
    @Override
    public ShowDTO getShow(Integer showNo) {
        Show show = showRepository.findById(showNo.longValue())
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다. ID: " + showNo));
        return ShowDTO.fromEntity(show);
    }

    // 전체 공연 회차 목록 조회
    @Override
    public List<ShowDTO> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(ShowDTO::fromEntity)
                .collect(Collectors.toList());
    }

    //상태값(예: 0:예약가능, 1:예정 등) 기준 공연 회차 목록 조회 (페이지네이션 적용)
    @Override
    public Page<ShowDTO> getShowsByStates(List<Integer> states, int page, int size) {
        List<ShowDTO> filteredList = showRepository.findAll()
                .stream()
                .filter(show -> states.contains(show.getShowState()))
                .map(ShowDTO::fromEntity)
                .collect(Collectors.toList());

        int start = Math.min(page * size, filteredList.size());
        int end = Math.min(start + size, filteredList.size());

        List<ShowDTO> pagedList = filteredList.subList(start, end);
        return new PageImpl<>(pagedList, PageRequest.of(page, size), filteredList.size());
    }

    //특정 공연 정보 ID 기준으로 예매 가능/예정인 회차 조회
    @Override
    public List<ShowDTO> getShowsByShowInfo(Integer showInfoId) {
        List<Show> shows = showRepository.findByShowInfo_Id(showInfoId.longValue());
        return shows.stream()
                .filter(show -> show.getShowState() == 0 || show.getShowState() == 1)
                .map(ShowDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
