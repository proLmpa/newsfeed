package com.newsfeed.board.user.service;

import com.newsfeed.board.common.jwt.JwtUtil;
import com.newsfeed.board.user.dto.UserRequestDto;
import com.newsfeed.board.user.dto.UserResponseDto;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void signup(UserRequestDto requestDto) {
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findById(id);
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("ID already exists");
        }

        // 사용자 등록
        UserEntity user = new UserEntity(id, password);
        userRepository.save(user);
    }


    public void login(UserRequestDto requestDto, HttpServletResponse res) {
        String id = requestDto.getId();
        String password = requestDto.getPassword();

        // 사용자 확인
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such user exists")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password mismatched");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        res.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getId()));
    }

    public UserResponseDto getProfile(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such user exists")
        );

        return new UserResponseDto(user);
    }

    @Transactional
    public ResponseEntity<UserResponseDto> updateProfile(UserRequestDto requestDto) {
        String id = requestDto.getId();
        String password = requestDto.getPassword();

        // 사용자 확인
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such user exists")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Password mismatched");
        }

        user.setUsername(requestDto.getUsername());
        user.setIntroduction(requestDto.getIntroduction());

        return ResponseEntity.ok().body(new UserResponseDto(user));
    }
}
