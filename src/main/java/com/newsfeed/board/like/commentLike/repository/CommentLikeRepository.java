package com.newsfeed.board.like.commentLike.repository;

import com.newsfeed.board.comment.entity.CommentEntity;
import com.newsfeed.board.like.commentLike.entity.CommentLikeEntity;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository <CommentLikeEntity, Long> {
    Optional<CommentLikeEntity> findByUserEntityAndCommentEntity(UserEntity user, CommentEntity comment);
}