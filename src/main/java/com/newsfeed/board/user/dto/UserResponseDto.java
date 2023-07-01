package com.newsfeed.board.user.dto;

import com.newsfeed.board.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private String id;
    private String password;
    private String username;
    private String introduction;

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.introduction = user.getIntroduction();
    }
}
