package com.newsfeed.board.post.dto;


import com.newsfeed.board.comment.dto.CommentResponseDto;
import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.post.entity.PostEntity;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private String title;
    private String contents;
    private String userId;
    private Integer like;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(PostEntity post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.userId = post.getUser().getId();
        this.like = post.getLikes();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = new ArrayList<>();
    }

    public PostResponseDto(PostEntity post, List<CommentEntity> commentList) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.userId = post.getUser().getId();
        this.like = post.getLikes();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.commentList = commentList.stream().map(CommentResponseDto::new).toList();
    }
}
