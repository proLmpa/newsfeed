package com.newsfeed.board.email;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CertifiResponsDto {
    private String config;
    private LocalDateTime createdAt;



    public CertifiResponsDto(String config, LocalDateTime createdAt) {
        this.config = config;
        this.createdAt = createdAt;
    }
}
