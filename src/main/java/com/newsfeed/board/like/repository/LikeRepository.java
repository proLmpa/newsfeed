package com.newsfeed.board.like.repository;

import com.newsfeed.board.like.entity.LikeEntity;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LikeRepository extends JpaRepository <LikeEntity, Long> {
    Optional<LikeEntity> findByUserEntityAndPostEntity(UserEntity userEntity, PostEntity postEntity);

}
