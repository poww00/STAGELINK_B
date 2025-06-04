package com.pro.controller.refund;

import com.pro.dto.RefundPreviewDto;
import com.pro.dto.RefundRequestDto;
import com.pro.dto.RefundResponseDto;
import com.pro.security.user.CustomUserDetails;
import com.pro.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/mypage/refunds")
public class RefundController {

    private final RefundService refundService;

    // 환불 로직
    @PostMapping
    public ResponseEntity<RefundResponseDto> requestRefund(
            @RequestBody RefundRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        Long reservationId = requestDto.getReservationNo();
        Long memberId = userDetails.getId(); // 로그인한 사용자의 ID

        RefundResponseDto response = refundService.processRefund(reservationId, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/preview")
    public ResponseEntity<RefundPreviewDto> previewRefund(
            @RequestParam("reservationId") Long reservationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        RefundPreviewDto preview = refundService.getRefundPreview(reservationId, userDetails.getId());
        return ResponseEntity.ok(preview);
    }



}
