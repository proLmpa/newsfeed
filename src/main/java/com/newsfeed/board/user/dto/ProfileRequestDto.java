package com.newsfeed.board.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDto {
    @Pattern(regexp="^[a-zA-Z0-9~`!@#$%^&*()-_+=]{8,15}$", message="새 비밀번호는 영대소문자와 숫자(0~9) 및 특수문자(~`!@#$%^&*()-_+=)로 이뤄진 8자 이상 15자 이하의 값으로 이뤄져야 합니다.")
    private String newPassword;
    private String username;
    private String introduction;
}
