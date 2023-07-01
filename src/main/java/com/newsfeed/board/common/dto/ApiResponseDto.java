package com.newsfeed.board.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString // 출력 시 각 field 명을 포함시키는 annotation
public class ApiResponseDto {
    // API result 반환을 위한 DTO
    // 상태 메시지와 상태 코드를 반환함
    private long statusCode;
    private String statusMessage;

    @Builder // 해당 annotation이 적힌 field는 반드시 초기화 되어야 한다.
    public ApiResponseDto(long statusCode, String statusMessage){
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
