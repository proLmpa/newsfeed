package com.newsfeed.board.follow.repository;

import com.newsfeed.board.follow.entity.FollowEntity;
import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    Optional<FollowEntity> findByFollowerUserAndFollowingUser(UserEntity followerUser, UserEntity followingUser);
}
