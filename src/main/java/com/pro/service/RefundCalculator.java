package com.pro.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RefundCalculator {

    /**
     * 환불 금액과 수수료를 계산하는 유틸 메서드
     * @param reservationDate 예매일
     * @param showDateTime 관람일시
     * @param totalAmount 총 결제금액 (원)
     * @return 환불 결과 (환불금액, 수수료 포함)
     */

    public static RefundResult calculate(LocalDateTime reservationDate, LocalDateTime showDateTime, int totalAmount) {
        LocalDateTime now = LocalDateTime.now();

        long daysSinceReservation = ChronoUnit.DAYS.between(reservationDate.toLocalDate(), now.toLocalDate());
        long daysBeforeShow = ChronoUnit.DAYS.between(now.toLocalDate(), showDateTime.toLocalDate());

        // 관람 당일 환불 불가
        if (!now.isBefore(showDateTime.toLocalDate().atStartOfDay())) {
            throw new IllegalArgumentException("관람일 당일은 환불이 불가합니다.");
        }

        int fee;

        // ✅ 1. 관람일 기준 먼저 판단 (우선순위)
        if (daysBeforeShow >= 10) {
            // 🎯 이 구간에서만 예매 7일 이내인 경우 무료!
            if (daysSinceReservation <= 7) {
                fee = 0;
            } else {
                fee = Math.min(4000, (int) (totalAmount * 0.1));
            }
        } else if (daysBeforeShow >= 7) {
            fee = (int) (totalAmount * 0.1);
        } else if (daysBeforeShow >= 3) {
            fee = (int) (totalAmount * 0.2);
        } else if (daysBeforeShow >= 1) {
            fee = (int) (totalAmount * 0.3);
        } else {
            throw new IllegalArgumentException("예상치 못한 환불 조건입니다.");
        }

        int refundAmount = totalAmount - fee;
        return new RefundResult(refundAmount, fee);
    }


    // 환불 결과를 담는 내부 클래스
    @Getter
    @AllArgsConstructor
    public static class RefundResult {
            private final int refundAmount; // 환불 금액
            private final int fee; // 수수료 금액
        }
}
