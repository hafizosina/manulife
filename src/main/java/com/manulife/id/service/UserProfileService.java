package com.manulife.id.service;

import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserProfileService {

    @Autowired
    private UserRepository repository;

    public UserProfileDto create(UserProfileDto request, HttpServletRequest servletRequest) {
        return null;
    }
}
