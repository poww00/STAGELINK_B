package com.pro.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_SHOWLOCATION") // 🏛️ 공연장 정보 테이블 매핑
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 PK
    @Column(name = "SHOWLOCATION_ID")
    private Long id; // 공연장 고유 ID

    @Column(name = "LOCATION_NAME", nullable = false)
    private String name; // 공연장 이름 (예: 예술의전당, 블루스퀘어 등)

    @Column(name = "LOCATION_ADDRESS", nullable = false)
    private String address; // 공연장 주소 (지도 검색 및 출력용)

    @Column(name = "FACILITY_ID", nullable = false)
    private String facilityId; // 외부 시스템(예: 시설 관리 시스템)에서 사용하는 식별자
}

