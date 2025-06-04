package com.pro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 잔여 좌석수를 등급별로 묶어 전달하는 DTO
 */
@Getter
@AllArgsConstructor
public class RemainingSeatDTO {
    private long vip;   // VIP 잔여
    private long r;     // R   잔여
    private long a;     // A   잔여
    private long s;     // S   잔여
}
