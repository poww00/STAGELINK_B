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
 * 배우 관련 비즈니스 로직을 처리하는 서비스 클래스
 * - 배우 상세 조회
 * - 출연작 포함 응답 구성
 */
@Service
@RequiredArgsConstructor // 생성자 주입 자동 생성
public class ActorService {

    // 배우 기본 정보 저장소
    private final ActorRepository actorRepository;

    // 배우-공연 출연 정보 저장소
    private final ActorShowRepository actorShowRepository;

    /**
     * 배우 상세 정보 조회
     * @param actorNo 배우 번호 (PK)
     * @return 배우 기본 정보 + 출연작 목록이 포함된 DTO
     */
    @Transactional(readOnly = true)
    public ActorDTO getActorDetail(Integer actorNo) {
        // 배우 존재 여부 확인
        Actor actor = actorRepository.findById(actorNo)
                .orElseThrow(() -> new RuntimeException("배우 정보를 찾을 수 없습니다."));

        // 해당 배우의 출연작 목록 조회 → DTO 변환
        List<ActorShowDTO> shows = actorShowRepository.findByActor_ActorNo(actorNo)
                .stream()
                .map(ActorShowDTO::fromEntity)
                .collect(Collectors.toList());

        // 배우 기본 정보 + 출연작 포함한 DTO 반환
        return ActorDTO.fromEntity(actor, shows);
    }
}
