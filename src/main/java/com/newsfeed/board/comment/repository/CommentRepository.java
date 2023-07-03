package com.newsfeed.board.comment.repository;

import com.newsfeed.board.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByPostIdOrderByCreatedAtDesc(Long postId);
}
