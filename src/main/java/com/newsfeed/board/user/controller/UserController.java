package com.newsfeed.board.user.controller;

import com.newsfeed.board.common.dto.ApiResponseDto;
import com.newsfeed.board.common.security.UserDetailsImpl;
import com.newsfeed.board.user.dto.PasswordRequestDto;
import com.newsfeed.board.user.dto.ProfileRequestDto;
import com.newsfeed.board.user.dto.UserRequestDto;
import com.newsfeed.board.user.dto.UserResponseDto;
import com.newsfeed.board.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
    public ResponseEntity<ApiResponseDto> signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외 처리
        ResponseEntity<ApiResponseDto> result = checkUserRequestDto(bindingResult);
        if(result != null) return result;

        try {
            userService.signup(requestDto);
        } catch(IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "ID_ALREADY_EXIST"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SIGN_UP_SUCCESS"));
    }
    @PostMapping("/user/code")
    public String checkedCode(@RequestParam String config) {
        return userService.checkedCode(config);
        }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponseDto> login(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult, HttpServletResponse res) {
        // Validation 예외 처리
        ResponseEntity<ApiResponseDto> result = checkUserRequestDto(bindingResult);
        if(result != null) return result;

        try {
            userService.login(requestDto, res);
        } catch(IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        }

        return ResponseEntity.ok().body(new ApiResponseDto(200L, "LOGIN_SUCCESS"));
    }

    @GetMapping("/user/profile")
    public UserResponseDto getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getProfile(userDetails.getId());
    }

    @PutMapping("/user/profile")
    public ResponseEntity<ApiResponseDto> updateProfile(@RequestBody ProfileRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            userService.updateProfile(requestDto, userDetails.getUser());
        } catch(IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_PROFILE_EDIT"));
    }

    @PutMapping("/user/password")
    public ResponseEntity<ApiResponseDto> updateProfilePassword(@Valid @RequestBody PasswordRequestDto requestDto, BindingResult bindingResult, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // Validation 예외 처리
        ResponseEntity<ApiResponseDto> result = checkUserRequestDto(bindingResult);
        if(result != null) return result;

        try {
            userService.updatePassword(requestDto, userDetails.getUser());
        } catch(IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, e.getMessage()));
        }
        return ResponseEntity.ok().body(new ApiResponseDto(200L, "SUCCESS_PASSWORD_EDIT"));
    }


    public ResponseEntity<ApiResponseDto> checkUserRequestDto(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0){
            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(new ApiResponseDto(400L, "INVALID_TYPE_VALUE"));
        }

        return null;
    }
}
