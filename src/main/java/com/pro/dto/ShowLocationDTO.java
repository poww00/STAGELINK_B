package com.pro.dto;

import com.pro.entity.ShowLocation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLocationDTO {
    private Long id;
    private String name;
    private String address;

    public static ShowLocationDTO fromEntity(ShowLocation entity) {
        return ShowLocationDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .build();
    }
}
