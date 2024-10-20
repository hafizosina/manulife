package com.manulife.id.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestPaging {
    private Integer pageNumber = 0;
    private Integer pageSize = 10;
}
