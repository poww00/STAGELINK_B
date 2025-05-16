package com.pro.service.Impl;

import com.pro.dto.ShowDTO;
import com.pro.entity.Show;
import com.pro.repository.ShowRepository;
import com.pro.service.ShowService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 공연 관련 비즈니스 로직을 처리하는 서비스 구현 클래스
@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;

    // 생성자 기반 의존성 주입
    public ShowServiceImpl(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    // 공연 ID로 공연 상세 정보 조회
    @Override
    public ShowDTO getShow(Integer showNo) {
        Show show = showRepository.findById(showNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 공연이 존재하지 않습니다. ID: " + showNo));
        return ShowDTO.fromEntity(show);
    }


    // 전체 공연 목록 조회
    @Override
    public List<ShowDTO> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(ShowDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 상태인 공연 조회
    @Override
    public List<ShowDTO> getShowsByStates(List<Integer> states) {
        return showRepository.findAll()
                .stream()
                .filter(show -> states.contains(show.getShowState()))
                .map(ShowDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
