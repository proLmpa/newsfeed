package com.newsfeed.board.post.repository;

import com.newsfeed.board.post.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllByOrderByCreatedAtDesc();
}
