package com.pro.dto;

import com.pro.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MypageHomeDto {
    private String nickname;
    private int reservationCount;
}
