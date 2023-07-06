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

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    public UserEntity(String id, String password, UserRoleEnum role,String email){
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
