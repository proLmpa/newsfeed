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
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username")
    private String username;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "email")
    private String email;


    public UserEntity(String id, String password, String email){
        this.id = id;
        this.password = password;
        this.email = email;
    }
}
