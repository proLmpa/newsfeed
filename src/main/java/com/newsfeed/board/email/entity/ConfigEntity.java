package com.newsfeed.board.email.entity;

import com.newsfeed.board.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "certification")
public class ConfigEntity extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    private String config;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    public ConfigEntity(String config) {
        this.config = config;
    }

    public ConfigEntity() {

    }
}
