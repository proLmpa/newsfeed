package com.newsfeed.board.email;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "certification")
public class ConfigEntity extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String config;

    public ConfigEntity(String config) {
        this.config = config;
    }
}
