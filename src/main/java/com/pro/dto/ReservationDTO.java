package com.pro.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {
    private Long userId; // memberId → userId
    private Long showId;
    private List<Long> seatIdList;
    private Integer totalAmount;
    private String impUid;
    private LocalDateTime reservationDate;
}
