package com.newsfeed.board.post.dto;

import com.newsfeed.board.post.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private String title;
    private String contents;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public PostResponseDto(PostEntity post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.userId = post.getUser().getId();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}
