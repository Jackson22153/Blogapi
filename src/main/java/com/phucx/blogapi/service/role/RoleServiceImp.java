package com.phucx.blogapi.service.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phucx.blogapi.model.Role;
import com.phucx.blogapi.repository.RoleRepository;

@Service
public class RoleServiceImp implements RoleService{
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Role getRole(String roleName) {
        return roleRepository.findByRole(roleName)
            .orElseThrow(()-> new RuntimeException("Role " + roleName + " does not found"));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRoles(List<String> roleNames) {
        return roleRepository.findAllByRoleNames(roleNames);
    }
    
}
