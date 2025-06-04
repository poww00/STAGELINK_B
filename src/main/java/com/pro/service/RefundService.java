package com.pro.service;


import com.pro.dto.RefundPreviewDto;
import com.pro.dto.RefundRequestDto;
import com.pro.dto.RefundResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface RefundService {
    RefundResponseDto processRefund(Long reservationId, Long userId);

    RefundPreviewDto getRefundPreview(Long reservationId, Long userId);

}
