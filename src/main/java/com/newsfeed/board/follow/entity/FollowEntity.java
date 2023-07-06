package com.newsfeed.board.follow.entity;

import com.newsfeed.board.common.auditing.Timestamped;
import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow", uniqueConstraints = {
        @UniqueConstraint(name = "UniqueFollowerUser", columnNames = {"following_user","follower_user"})
}) // 동일한 "following_user"와 "follower_user" 값의 조합이 두 번 이상 등록되는 것을 방지 => 두 컬럼의 조합이 있다는 것은 이미 팔로우 관계라는 의미
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
