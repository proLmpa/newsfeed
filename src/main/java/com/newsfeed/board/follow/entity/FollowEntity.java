package com.newsfeed.board.follow.entity;

import com.newsfeed.board.common.auditing.Timestamped;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class FollowEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_user")
    private UserEntity followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_user")
    private UserEntity followerUser;



    public FollowEntity (UserEntity followingUser, UserEntity followerUser) {
        this.followingUser = followingUser;
        this.followerUser = followerUser;
    }


}
