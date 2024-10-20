package com.manulife.id.controller;

import com.manulife.id.dto.ResponseDto;
import com.manulife.id.exception.ProcessException;
import com.manulife.id.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(value = "/test")
@Tag(name = "Test")
public class SandBoxController {

    @PostMapping("/base-success")
    @Operation(summary = "Base Success", method = "POST")
    public ResponseDto<String> baseSuccess(@RequestParam("code") String code, HttpServletRequest servletRequest) {
        if ( Objects.equals(code, "200") ){
            return ResponseUtil.success("Success");
        }else {
            throw new ProcessException("Unknown Error", code);
        }
    }
}
