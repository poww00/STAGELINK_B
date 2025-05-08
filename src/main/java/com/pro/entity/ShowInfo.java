package com.pro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_showinfo")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowInfo {

    // 공연 정보 번호 (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_info")
    private Long id;

    // 포스터 이미지 경로
    @Column(name = "show_poster", nullable = false, length = 255)
    private String poster;

    // 공연 제목
    @Column(name = "show_name", nullable = false, length = 50)
    private String name;

    // 공연 설명
    @Column(name = "show_explain", length = 300)
    private String explain;

    // 장르/카테고리
    @Column(name = "show_category", length = 10)
    private String category;

    // 관람 가능 연령
    @Column(name = "show_age")
    private Integer age;

    // 공연 러닝타임
    @Column(name = "show_duration")
    private String duration;

    // 공연 장소 ID
    @Column(name = "show_location")
    private Integer locationId;

    // 공연 스타일 이미지1
    @Column(name = "show_styurl1")
    private String styUrl1;

    // 공연 스타일 이미지2
    @Column(name = "show_styurl2")
    private String styUrl2;

    // 공연 스타일 이미지3
    @Column(name = "show_styurl3")
    private String styUrl3;

    // 공연 스타일 이미지4
    @Column(name = "show_styurl4")
    private String styUrl4;

    /**
     * 공연 회차 리스트
     * Show 엔티티에서 showInfo 필드로 연관됨
     */
    @OneToMany(mappedBy = "showInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Show> shows = new ArrayList<>();
}
