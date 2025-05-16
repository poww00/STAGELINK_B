package com.pro.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostResponseDto {
    private int postNo;
    private String postTitle;
    private String postContent;
    private int postRating;
    private String nickname;
    private String postRegisterDate;
    private String showName;

    public PostResponseDto() {}

    public PostResponseDto(int postNo, String postTitle, String postContent,
                           int postRating, String nickname, String postRegisterDate,
                           String showName) {
        this.postNo = postNo;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postRating = postRating;
        this.nickname = nickname;
        this.postRegisterDate = postRegisterDate;
        this.showName = showName;
    }

    // Getter/Setter 생략 가능
}
