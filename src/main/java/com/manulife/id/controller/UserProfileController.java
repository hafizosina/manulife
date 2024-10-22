package com.manulife.id.controller;


import com.manulife.id.dto.*;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @PutMapping("/upload")
    @Operation(summary = "2. Upload User Image", description = "Upload User Image")
    public ResponseDto<String> uploadImage(@RequestBody ImageRequestDto request, HttpServletRequest servletRequest) {
        service.uploadImage(request, servletRequest);
        return ResponseUtil.success("Success");
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getUserImage(@RequestParam("username") String username, HttpServletRequest servletRequest) {
        byte[] image = service.getImage(username, servletRequest);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
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
    @GetMapping("/exportpdf")
    @Operation(summary = "6. Download PDF File", description = "Download PDF File")
    public ResponseEntity<Resource> exportPdf(HttpServletRequest servletRequest) {
        String fileName = "List User.pdf";
        InputStreamResource file = new InputStreamResource(service.exportPdf(servletRequest));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(file);
    }
}
