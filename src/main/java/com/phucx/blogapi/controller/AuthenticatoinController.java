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
import com.phucx.blogapi.model.UserRoles;
import com.phucx.blogapi.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class AuthenticatoinController {
    @Autowired
    private UserService userService;

    @Operation(
        summary = "Register a user",
        description = "Create a new user",
        tags = {"post", "public"})
    @PostMapping("/register")
    public ResponseEntity<ResponseFormat> register(@RequestBody UserDetail userDetail){
        Boolean status = userService.registerUser(userDetail);
        return ResponseEntity.ok().body(new ResponseFormat(status));
    }

    @Operation(
        summary = "Log in to the server",
        description = "Use Basic Authorization which set username and password on the request header for server to authenticate",
        tags = {"get", "public"})
    @GetMapping("/login")
    public ResponseEntity<UserRoles> login(Authentication authentication){
        String username = authentication.getName();
        UserRoles user = userService.getUserRoles(username);
        return ResponseEntity.ok().body(user);
    }

}
