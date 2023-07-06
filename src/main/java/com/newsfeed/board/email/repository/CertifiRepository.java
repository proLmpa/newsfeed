package com.newsfeed.board.email.repository;


import com.newsfeed.board.email.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.Optional;

public interface CertifiRepository extends JpaRepository<ConfigEntity, String> {

//    ConfigEntity findByConfig(String config);
    Optional<ConfigEntity> findByConfig(String config);
    ConfigEntity findUserIdByConfig(String config);
}
