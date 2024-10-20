package com.manulife.id.util;

import com.manulife.id.dto.ResponseDto;
import com.manulife.id.dto.ResponsePagingDto;
import org.springframework.data.domain.Page;
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
    public static <T> ResponsePagingDto<T> paging(T pageData, Page page) {
        ResponsePagingDto<T> response = new ResponsePagingDto<>();
        response.setStatus(HttpStatus.OK.value());
        response.setTimestamp(LocalDateTime.now());
        response.setTotalPages(page.getTotalPages());
        response.setPageNumber(page.getNumber());
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setResult(pageData);
        return response;
    }
}
