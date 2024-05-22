package com.phucx.blogapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.phucx.blogapi.model.ResponseFormat;
import com.phucx.blogapi.model.UserDetail;
import com.phucx.blogapi.model.UserRoleInfoDTO;
import com.phucx.blogapi.service.user.UserService;

@RestController
public class AuthenticatoinController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseFormat> register(@RequestBody UserDetail userDetail){
        Boolean status = userService.registerUser(userDetail);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @GetMapping("/login")
    public ResponseEntity<UserRoleInfoDTO> login(Authentication authentication){
        String username = authentication.getName();
        UserRoleInfoDTO user = userService.getUserRoles(username);
        return ResponseEntity.ok().body(user);
    }
}
