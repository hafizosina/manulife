package com.manulife.id.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;
    private String fullname;
    private String email;
    private String username;
    private String password;
    private String role;
    private String address;
    private String image;
}
