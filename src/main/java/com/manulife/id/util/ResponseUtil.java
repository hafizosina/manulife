package com.manulife.id.util;

import com.manulife.id.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.OK;

@Component
public class ResponseUtil {

    public static <T> ResponseDto<T> success(T result) {
        ResponseDto<T> response = new ResponseDto<>();
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(LocalDateTime.now());
        response.setResult(result);
        return response;
    }
}
