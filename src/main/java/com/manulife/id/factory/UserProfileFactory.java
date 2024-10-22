package com.manulife.id.factory;

import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.model.MasterUser;
import org.springframework.stereotype.Component;


@Component
public class UserProfileFactory implements AbstractFactory<MasterUser, UserProfileDto>{
    @Override
    public UserProfileDto buildDto(MasterUser entity) {
        return UserProfileDto.builder()
                .id(entity.getId())
                .fullname(entity.getFullname())
                .email(entity.getEmail())
                .username(entity.getUsername())
                .password(null)
                .role(entity.getRole())
                .address(entity.getAddress())
                .image(entity.getImage())
                .build();
    }

    @Override
    public MasterUser fillEntity(UserProfileDto dto, MasterUser entity) {
        entity.setFullname(dto.getFullname());
        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        if (entity.getId() == null) {
            // set password only for new user,
            // update password done seperately
            entity.setPassword(dto.getPassword());
        }
        entity.setRole(dto.getRole());
        entity.setAddress(dto.getAddress());
        // image upload seperately
        // entity.setImage(dto.getImage());
        return entity;
    }
}
