package com.newsfeed.board.user.service;

import com.newsfeed.board.common.jwt.JwtUtil;
import com.newsfeed.board.email.entity.ConfigEntity;
import com.newsfeed.board.email.repository.CertifiRepository;
import com.newsfeed.board.email.service.EmailServiceImpl;
import com.newsfeed.board.user.dto.PasswordRequestDto;
import com.newsfeed.board.user.dto.ProfileRequestDto;
import com.newsfeed.board.user.dto.UserRequestDto;
import com.newsfeed.board.user.dto.UserResponseDto;
import com.newsfeed.board.user.entity.UserEntity;
import com.newsfeed.board.user.entity.UserRoleEnum;
import com.newsfeed.board.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CertifiRepository certifiRepository;
    private final JwtUtil jwtUtil;
    private final EmailServiceImpl emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CertifiRepository certifiRepository, JwtUtil jwtUtil, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.certifiRepository = certifiRepository;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    @Transactional
    public void signup(UserRequestDto requestDto) throws Exception {
        String id = requestDto.getId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        UserRoleEnum role =  UserRoleEnum.NONUSER;
        // 회원 중복 확인
        Optional<UserEntity> checkUsername = userRepository.findById(id);
        if(checkUsername.isPresent()){
            throw new IllegalArgumentException("ID already exists");
        }
        UserEntity user = new UserEntity(id, password, role, email);
        userRepository.save(user);
        //email 인증발송
        // ConfigEntity 타입으로 ePw인 인증코드 저장
            ConfigEntity config = new ConfigEntity(user,emailService.sendSimpleMessage(email));
            //certifirepostiroy에 인증코드 저장
            certifiRepository.save(config);

    }
    @Transactional
    public String checkedCode(String config){
        LocalDateTime currentTime = LocalDateTime.now();//현재시간저장
      Optional <ConfigEntity> configEntity = certifiRepository.findByConfig(config);//입력받아온 인증코드로부터 저장된 인증코드 불러오기

        String con = configEntity.get().getUser().getId();//con 은 certifirepository에 저장된 config를 통해서 userid를 골라와서 저장한다.

        System.out.println(con);
        LocalDateTime sentAt= configEntity.orElseThrow().getCreatedAt();
        LocalDateTime aftertime = sentAt.plusMinutes(1);
        String DBconfig = configEntity.orElseThrow().getConfig();

        if (!(currentTime.isAfter(aftertime))) {
           if (DBconfig.equals(config)){
               userRepository.updateUserRoleToUser(con);//con과 일치하는 user정보의 role을 nonuser에서 user로 변경한다.
               return "인증되었습니다.";
           }else {
               return "인증번호가 틀립니다.";
           }

        }else {
        return "인증번호가 만료되었습니다.";
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
