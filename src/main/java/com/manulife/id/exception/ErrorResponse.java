package com.manulife.id.exception;

import com.manulife.id.dto.BaseResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse extends BaseResponseDto {

    private final String error;
    private final String message;
    private final String source;
    private final String code;


    public ErrorResponse(ProcessException ex) {
        super(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        this.message = ex.getMessage();
        this.source = ex.getSource();
        this.code = ex.getCode();
    }

    public ErrorResponse(BadRequestException ex) {
        super(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        this.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        this.message = ex.getMessage();
        this.source = ex.getSource();
        this.code = ex.getCode();
    }
}
