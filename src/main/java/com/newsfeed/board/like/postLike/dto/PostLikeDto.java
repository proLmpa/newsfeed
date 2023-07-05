package com.newsfeed.board.like.postLike.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeDto {
    private String userId;

    public PostLikeDto(String userId, Long postId) {
        this.userId = userId;
    }
}
