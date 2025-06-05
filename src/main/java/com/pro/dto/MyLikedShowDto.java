package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyLikedShowDto {

    private Integer showNo;
    private String showName;
    private String poster;
    private String period;
    private boolean available;

    public MyLikedShowDto(Integer showNo, String showName, String poster, String period, boolean available) {
        this.showNo = showNo;
        this.showName = showName;
        this.poster = poster;
        this.period = period;
        this.available = available;
    }

    public MyLikedShowDto() {}
}
