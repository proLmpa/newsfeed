package com.newsfeed.board.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "introduction")
    private String introduction;

    public UserEntity(String id, String password){
        this.id = id;
        this.password = password;
    }
}
