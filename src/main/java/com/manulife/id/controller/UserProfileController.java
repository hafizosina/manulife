package com.manulife.id.controller;


import com.manulife.id.dto.RequestPaging;
import com.manulife.id.dto.ResponseDto;
import com.manulife.id.dto.ResponsePagingDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(value = "/user-profile")
@Tag(name = "User Profile", description = "Operations related to user profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

    @PostMapping("/")
    @Operation(summary = "1. Create User Profile", description = "Create a new user profile")
    public ResponseDto<UserProfileDto> create(@RequestBody UserProfileDto request, HttpServletRequest servletRequest) {
        UserProfileDto dto = service.create(request, servletRequest);
        return ResponseUtil.success(dto);
    }

    @PutMapping("/")
    @Operation(summary = "2. Update User Profile", description = "Update an existing user profile")
    public ResponseDto<UserProfileDto> update(@RequestBody UserProfileDto request, HttpServletRequest servletRequest) {
        UserProfileDto dto = service.update(request, servletRequest);
        return ResponseUtil.success(dto);
    }

    @DeleteMapping("/")
    @Operation(summary = "3. Delete User Profile", description = "Delete an existing user profile by username")
    public ResponseDto<String> delete(@RequestParam("username") String username, HttpServletRequest servletRequest) {
        service.delete(username, servletRequest);
        return ResponseUtil.success("Success");
    }

    @GetMapping("/")
    @Operation(summary = "4. Get User Profile", description = "Get user profile by username")
    public ResponseDto<List<UserProfileDto>> get(@RequestParam("username") String username, HttpServletRequest servletRequest) {
        List<UserProfileDto> listDto = service.get(username, servletRequest);
        return ResponseUtil.success(listDto);
    }

    @GetMapping("/paging")
    @Operation(summary = "5. Get Paged User Profiles", description = "Get user profiles with pagination")
    public ResponsePagingDto<List<UserProfileDto>> getPaging(@RequestBody RequestPaging request, HttpServletRequest servletRequest) {
        return service.getPaging(request, servletRequest);
    }
}
