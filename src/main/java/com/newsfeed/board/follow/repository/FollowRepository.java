package com.newsfeed.board.follow.repository;

import com.newsfeed.board.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
}
