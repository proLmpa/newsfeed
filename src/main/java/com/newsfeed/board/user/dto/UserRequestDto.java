package com.newsfeed.board.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank
    @Pattern(regexp="^[a-zA-Z][a-zA-Z0-9._-]{1,14}$", message="영대소문자와 숫자(0~9) 및 특수문자(._-)로 이뤄진 4자 이상 10자 이하의 값만 입력 가능합니다.")
    private String id;

    @NotBlank
    @Pattern(regexp="^[a-zA-Z0-9~`!@#$%^&*()-_+=]{8,15}$", message="영대소문자와 숫자(0~9) 및 특수문자(~`!@#$%^&*()-_+=)로 이뤄진 8자 이상 15자 이하의 값으로 이뤄졌습니다.")
    private String password;

    @Pattern(regexp="^[a-zA-Z0-9~`!@#$%^&*()-_+=]{8,15}$", message="영대소문자와 숫자(0~9) 및 특수문자(~`!@#$%^&*()-_+=)로 이뤄진 8자 이상 15자 이하의 값으로 이뤄졌습니다.")
    private String newPassword;

    private String username;
    private String introduction;
}
