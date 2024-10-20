package com.manulife.id.controller;


import com.manulife.id.dto.ResponseDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user-profile")
@Tag(name = "User Profile")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @PostMapping("/create")
    @Operation(summary = "Create", method = "POST")
    public ResponseDto<UserProfileDto> create(@RequestBody UserProfileDto request, HttpServletRequest servletRequest) {
        UserProfileDto dto = service.create(request, servletRequest);

        return ResponseUtil.created(dto);
    }


}
