package com.phucx.blogapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class UserDetail {
    private String username;
    private String password;
    private List<String> roles;

    public UserDetail(String username, String password) {
        this();
        this.username = username;
        this.password = password;
    }
    public UserDetail(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
    public UserDetail() {
        this.roles = new ArrayList<>();
    }
}
