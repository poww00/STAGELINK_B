package com.pro.controller;

import com.pro.dto.ActorDTO;
import com.pro.dto.ActorShowDTO;
import com.pro.service.ActorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 배우 관련 API 컨트롤러
 * - 배우 상세 정보 및 출연작 조회 기능 제공
 */
@RestController
@RequestMapping("/api/actors")
@RequiredArgsConstructor
public class ActorController {

    private final ActorService actorService;

    // [1] 배우 상세 정보 조회 (기본 정보 + 출연작 포함)
    // 예: GET /api/actors/3
    @GetMapping("/{actorNo}")
    public ResponseEntity<ActorDTO> getActorDetail(@PathVariable Integer actorNo) {
        ActorDTO actorDTO = actorService.getActorDetail(actorNo);
        return ResponseEntity.ok(actorDTO);
    }

    // [2] 배우 출연작 목록만 별도로 조회
    // 예: GET /api/actors/3/works
    @GetMapping("/{actorNo}/works")
    public ResponseEntity<List<ActorShowDTO>> getActorWorks(@PathVariable Integer actorNo) {
        List<ActorShowDTO> works = actorService.getActorWorks(actorNo);
        return ResponseEntity.ok(works);
    }
}
