package com.pro.controller.reservation;

import com.pro.dto.ReservationDTO;
import com.pro.dto.ReservationDetailDTO;
import com.pro.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // [1] 임시 예약 생성
    @PostMapping("/temp")
    public ResponseEntity<?> createTempReservation(@RequestBody ReservationDTO dto) {
        List<Long> resNos = reservationService.createTempReservation(dto);
        return ResponseEntity.ok(Map.of("reservationNoList", resNos));
    }

    // [2] 결제 확정 및 검증
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmReservation(@RequestBody ReservationDTO dto) {
        String token = getIamportAccessToken();
        Map<String, Object> pay = verifyIamportPayment(dto.getImpUid(), token);

        if (pay == null || !"paid".equals(pay.get("status"))) {
            return ResponseEntity.badRequest().body("결제 상태가 유효하지 않습니다.");
        }

        int paid = ((Number) pay.get("amount")).intValue();
        if (paid != dto.getTotalAmount()) {
            return ResponseEntity.badRequest().body("결제 금액 불일치");
        }

        List<Long> list = dto.getReservationNoList();
        if ((list == null || list.isEmpty()) && dto.getReservationNo() != null) {
            list = List.of(dto.getReservationNo());
        }
        if (list == null || list.isEmpty()) {
            return ResponseEntity.badRequest().body("reservationNoList 가 없습니다.");
        }

        reservationService.confirmReservation(list);
        return ResponseEntity.ok("결제 및 예매가 확정되었습니다!");
    }

    // [3] 예약 취소
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestBody ReservationDTO dto) {
        List<Long> list = dto.getReservationNoList();
        if ((list == null || list.isEmpty()) && dto.getReservationNo() != null) {
            list = List.of(dto.getReservationNo());
        }
        if (list == null || list.isEmpty()) {
            return ResponseEntity.badRequest().body("reservationNoList 가 없습니다.");
        }

        reservationService.cancelReservation(list);
        return ResponseEntity.ok("예약이 취소되었습니다.");
    }

    // [4] 예약 상세 조회 (단건)
    @GetMapping("/{reservationNo}")
    public ResponseEntity<ReservationDetailDTO> getDetail(@PathVariable Long reservationNo) {
        return ResponseEntity.ok(reservationService.getReservationDetail(reservationNo));
    }

    // [5] 포트원 AccessToken 발급
    private String getIamportAccessToken() {
        RestTemplate rest = new RestTemplate();
        JSONObject body = new JSONObject();
        body.put("imp_key", "1620104317130120");
        body.put("imp_secret", "9smVIL5yGSLbHi59L6WhBUVbxjRPhYUL2W2Qs8A9Rs4djglEdBAJYvh7v1EYcXH04m4dqAgF9W3nYHYu");

        ResponseEntity<Map> resp = rest.postForEntity(
                "https://api.iamport.kr/users/getToken",
                new HttpEntity<>(body.toString(), jsonHeader()), Map.class);

        return (String) ((Map<?, ?>) resp.getBody().get("response")).get("access_token");
    }

    // [6] 포트원 결제 검증
    private Map<String, Object> verifyIamportPayment(String impUid, String token) {
        RestTemplate rest = new RestTemplate();
        HttpEntity<?> req = new HttpEntity<>(authHeader(token));
        try {
            ResponseEntity<Map> resp = rest.exchange(
                    "https://api.iamport.kr/payments/" + impUid,
                    HttpMethod.GET, req, Map.class);
            return (Map<String, Object>) resp.getBody().get("response");
        } catch (HttpClientErrorException.NotFound e) {
            throw e;
        }
    }

    // [7] JSON 헤더 생성
    private HttpHeaders jsonHeader() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        return h;
    }

    // [8] 인증 헤더 생성
    private HttpHeaders authHeader(String token) {
        HttpHeaders h = new HttpHeaders();
        h.set("Authorization", token);
        return h;
    }
}
