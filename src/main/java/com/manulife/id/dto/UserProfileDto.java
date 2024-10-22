package com.manulife.id.dto;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    @Hidden
    private Long id;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private String role;
    private String address;
    private byte[] image;
    private String imageBase64;
}
