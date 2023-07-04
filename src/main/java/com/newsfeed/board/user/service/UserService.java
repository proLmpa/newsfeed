package com.newsfeed.board.user.service;

import com.newsfeed.board.common.jwt.JwtUtil;
import com.newsfeed.board.email.CertificationDto;
import com.newsfeed.board.email.EmailServiceImpl;
import com.newsfeed.board.user.dto.PasswordRequestDto;
import com.newsfeed.board.user.dto.ProfileRequestDto;
import com.newsfeed.board.user.dto.UserRequestDto;
import com.newsfeed.board.user.dto.UserResponseDto;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailServiceImpl emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Transactional
    public void signup(UserRequestDto requestDto, CertificationDto certificationDto) throws Exception {
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        String config = certificationDto.getConfig();
        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findById(id);
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("ID already exists");
        }

        //email 인증발송 및 확인
        String confirm = emailService.sendSimpleMessage(email);
        emailService.verifyEmail(config);
        if (confirm.equals(config)){
            // 사용자 등록
            UserEntity user = new UserEntity(id, password, email);
            userRepository.save(user);
        }else {
            throw new IllegalArgumentException("인증번호가 틀립니다.");
        }


    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public UserResponseDto getProfile(String id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such user exists")
        );

        return new UserResponseDto(user);
    }

    @Transactional
    public void updateProfile(ProfileRequestDto requestDto, UserEntity userEntity) {
        String id = userEntity.getId();
        String password = userEntity.getPassword();

        UserEntity user = checkIdAndPassword(id, password);

        user.setUsername(requestDto.getUsername());
        user.setIntroduction(requestDto.getIntroduction());
    }

    @Transactional
    public void updatePassword(PasswordRequestDto requestDto, UserEntity userEntity) {
        String id = userEntity.getId();
        String password = userEntity.getPassword();

        UserEntity user = checkIdAndPassword(id, password);

        if(passwordEncoder.matches(requestDto.getPassword(), password)){
            // 새 비밀번호 두 번 확인하기
            if(requestDto.getNewPassword1().equals(requestDto.getNewPassword2())){
                user.setPassword(passwordEncoder.encode(requestDto.getNewPassword1()));
            } else {
                throw new IllegalArgumentException("New password mismatched");
            }
        } else {
            throw new IllegalArgumentException("Password mismatched");
        }

    }

    public UserEntity checkIdAndPassword(String id, String password) {
        // 사용자 확인
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("No such user exists")
        );

        // 비밀번호 확인
        if (!password.equals(user.getPassword())) {
            throw new IllegalArgumentException("Password mismatched");
        }

        return user;
    }
}
