package com.pro.controller;

import com.pro.dto.ActorDTO;
import com.pro.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 배우 관련 API 컨트롤러
 * - 배우 상세 정보 조회 엔드포인트 제공
 */
@RestController
@RequestMapping("/api/actors") // 공통 URL prefix
@RequiredArgsConstructor // 생성자 주입 자동 생성
public class ActorController {

    private final ActorService actorService;

    /**
     * [GET] /api/actors/{actorNo}
     * 특정 배우의 상세 정보를 조회 (출연작 포함)
     *
     * @param actorNo 조회할 배우의 고유 번호
     * @return 배우 기본 정보 + 출연작 목록을 담은 DTO
     */
    @GetMapping("/{actorNo}")
    public ResponseEntity<ActorDTO> getActorDetail(@PathVariable Integer actorNo) {
        ActorDTO actorDTO = actorService.getActorDetail(actorNo);
        return ResponseEntity.ok(actorDTO); // 200 OK + JSON 응답
    }
}
