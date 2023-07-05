package com.newsfeed.board.email;


import com.newsfeed.board.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CertifiRepository extends JpaRepository<ConfigEntity, Long> {

    ConfigEntity findByConfig(String config);
}
