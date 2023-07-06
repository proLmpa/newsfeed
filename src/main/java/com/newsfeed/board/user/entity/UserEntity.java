package com.newsfeed.board.user.entity;

import com.newsfeed.board.follow.entity.FollowEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "introduction")
    private String introduction;

    @OneToMany(mappedBy = "followingUser")
    private List<FollowEntity> followingList = new ArrayList<>();

    @OneToMany (mappedBy = "followerUser")
    private List<FollowEntity> followerList = new ArrayList<>();

    public UserEntity(String id, String password){
        this.id = id;
        this.password = password;
    }
}
