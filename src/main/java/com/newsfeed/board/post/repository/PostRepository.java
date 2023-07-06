package com.newsfeed.board.post.repository;

import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findAllByOrderByCreatedAtDesc();

    List<PostEntity> findByUser(UserEntity user);
}
