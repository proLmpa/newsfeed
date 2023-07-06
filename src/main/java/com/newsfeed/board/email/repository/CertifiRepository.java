package com.newsfeed.board.email.repository;


import com.newsfeed.board.email.entity.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertifiRepository extends JpaRepository<ConfigEntity, Long> {

    ConfigEntity findByConfig(String config);
}
