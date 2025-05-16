package com.pro.dto;

public class PostRequestDto {
    private String postTitle;
    private String postContent;
    private int postRating;
    private Long postShowNo;

    public String getPostTitle() { return postTitle; }
    public void setPostTitle(String postTitle) { this.postTitle = postTitle; }

    public String getPostContent() { return postContent; }
    public void setPostContent(String postContent) { this.postContent = postContent; }

    public int getPostRating() { return postRating; }
    public void setPostRating(int postRating) { this.postRating = postRating; }

    public Long getPostShowNo() { return postShowNo; }
    public void setPostShowNo(Long postShowNo) { this.postShowNo = postShowNo; }
}
