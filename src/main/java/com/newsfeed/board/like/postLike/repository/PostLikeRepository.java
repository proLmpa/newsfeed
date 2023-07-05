package com.newsfeed.board.like.postLike.repository;

import com.newsfeed.board.like.postLike.entity.PostLikeEntity;
import com.newsfeed.board.post.entity.PostEntity;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostLikeRepository extends JpaRepository <PostLikeEntity, Long> {
    Optional<PostLikeEntity> findByUserEntityAndPostEntity(UserEntity userEntity, PostEntity postEntity);
}
