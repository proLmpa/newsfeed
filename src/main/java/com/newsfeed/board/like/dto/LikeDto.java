package com.newsfeed.board.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikeDto {
    private Long userId;
    private Long postId;

    public LikeDto(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
