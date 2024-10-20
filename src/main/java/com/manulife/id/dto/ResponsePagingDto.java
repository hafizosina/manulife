package com.manulife.id.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponsePagingDto<T> extends BaseResponseDto {

    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private T result;
}
