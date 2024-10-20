package com.manulife.id.controller;


import com.manulife.id.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService service;

}
