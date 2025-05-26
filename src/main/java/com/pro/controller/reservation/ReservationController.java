package com.pro.controller.reservation;

import com.pro.dto.ReservationDTO;
import com.pro.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // [아임포트용] 결제 검증 및 예매 처리
    @PostMapping("/payment/verify")
    public ResponseEntity<?> verifyAndReserve(@RequestBody ReservationDTO dto) {
        try {
            String accessToken = getIamportAccessToken();
            Map<String, Object> paymentInfo = verifyIamportPayment(dto.getImpUid(), accessToken);

            // 결제 상태 및 금액 검증
            if (paymentInfo == null || !"paid".equals(paymentInfo.get("status"))) {
                return ResponseEntity.badRequest().body("결제 내역이 유효하지 않거나, 결제상태가 완료되지 않았습니다.");
            }
            int paidAmount = ((Number) paymentInfo.get("amount")).intValue();
            if (paidAmount != dto.getTotalAmount()) {
                return ResponseEntity.badRequest().body("결제 금액이 일치하지 않습니다.");
            }

            // 예매(좌석 예약, Reservation 저장 등)
            reservationService.createReservation(dto);

            return ResponseEntity.ok("예매 및 결제 정보 저장이 완료되었습니다!");
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.badRequest().body("존재하지 않는 결제 정보입니다. 결제 실패!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 검증 또는 예매에 실패했습니다: " + e.getMessage());
        }
    }

    // 아임포트 인증
    private String getIamportAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("imp_key", "1620104317130120");
        body.put("imp_secret", "9smVIL5yGSLbHi59L6WhBUVbxjRPhYUL2W2Qs8A9Rs4djglEdBAJYvh7v1EYcXH04m4dqAgF9W3nYHYu");

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.iamport.kr/users/getToken", entity, Map.class
        );
        Map<String, Object> resp = (Map<String, Object>) response.getBody().get("response");
        return (String) resp.get("access_token");
    }

    // 결제내역 검증
    private Map<String, Object> verifyIamportPayment(String impUid, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.iamport.kr/payments/" + impUid,
                    HttpMethod.GET, entity, Map.class
            );
            return (Map<String, Object>) response.getBody().get("response");
        } catch (HttpClientErrorException.NotFound e) {
            throw e; // Controller에서 처리
        }
    }
}
