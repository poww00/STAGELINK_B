package com.pro.dto;

import com.pro.entity.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostResponseDto {

    private Long postNo;
    private Long memberNo;
    private String title;
    private String content;
    private LocalDateTime registerDate;

    //  Post 엔티티를 Dto로 변환하는 생성자
    public PostResponseDto(Post post) {
        this.postNo = post.getPostNo();
        this.memberNo = post.getMemberNo();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.registerDate = post.getRegisterDate();
    }
}
