package com.pro.service;

import com.pro.dto.ActorDTO;
import com.pro.dto.ActorShowDTO;
import com.pro.entity.Actor;
import com.pro.repository.ActorRepository;
import com.pro.repository.ActorShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ActorService
 * 배우 관련 비즈니스 로직을 처리하는 서비스 클래스
 * - 배우 상세 조회
 * - 출연작 조회
 */
@Service
@RequiredArgsConstructor // 생성자 기반 의존성 주입
public class ActorService {

    private final ActorRepository actorRepository;             // 배우 정보 저장소
    private final ActorShowRepository actorShowRepository;     // 배우-공연 출연 관계 저장소

    // 배우 상세 정보 + 출연작 목록 조회
    @Transactional(readOnly = true)
    public ActorDTO getActorDetail(Integer actorNo) {
        // 배우 조회 (없을 시 예외)
        Actor actor = actorRepository.findById(actorNo)
                .orElseThrow(() -> new RuntimeException("배우 정보를 찾을 수 없습니다."));

        // 해당 배우의 출연작 리스트 조회 및 DTO 변환
        List<ActorShowDTO> shows = actorShowRepository.findByActor_ActorNo(actorNo)
                .stream()
                .map(ActorShowDTO::fromEntity)
                .collect(Collectors.toList());

        // 배우 정보 + 출연작 포함한 DTO 생성
        return ActorDTO.fromEntity(actor, shows);
    }

    //배우가 출연한 공연 목록만 조회 (단순 리스트)
    @Transactional(readOnly = true)
    public List<ActorShowDTO> getActorWorks(Integer actorNo) {
        return actorShowRepository.findByActor_ActorNo(actorNo)
                .stream()
                .map(ActorShowDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
