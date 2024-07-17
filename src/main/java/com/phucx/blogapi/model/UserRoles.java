package com.phucx.blogapi.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class UserRoles {
    private Integer id;
    private String username;
    private List<String> roles;
    public UserRoles(Integer id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
    public UserRoles(Integer id, String username) {
        this();
        this.id = id;
        this.username = username;
    }
    public UserRoles() {
        this.roles = new ArrayList<>();
    }
}
