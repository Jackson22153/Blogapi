package com.phucx.blogapi.service.role;

import java.util.List;

import com.phucx.blogapi.model.Role;

public interface RoleService {
    public Role getRole(String roleName);
    public List<Role> getAllRoles();
    public List<Role> getRoles(List<String> roleNames);
    
}
