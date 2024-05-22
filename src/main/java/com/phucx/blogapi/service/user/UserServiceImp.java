package com.phucx.blogapi.service.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phucx.blogapi.constant.RoleConstant;
import com.phucx.blogapi.exception.UserAuthenticationException;
import com.phucx.blogapi.model.Role;
import com.phucx.blogapi.model.User;
import com.phucx.blogapi.model.UserDetail;
import com.phucx.blogapi.model.UserRoleInfo;
import com.phucx.blogapi.model.UserRoleInfoDTO;
import com.phucx.blogapi.repository.UserRepository;
import com.phucx.blogapi.repository.UserRoleInfoRepository;
import com.phucx.blogapi.service.role.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleInfoRepository userRoleInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public Boolean authenticate(String username, String password) {
        log.info("username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(()-> new UserAuthenticationException("Invalid Username or Password"));
        if(passwordEncoder.matches(password, user.getPassword())) return true;
        return false;
    }

    @Override
    public User getUser(int id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("User " + id + " does not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User " + username + " does not found"));
    }

    @Override
    public Integer getUserID(String username) {
        return this.getUserByUsername(username).getId();
    }

    @Override
    public UserRoleInfoDTO getUserRoles(String username) {
        List<UserRoleInfo> userRoleInfos = userRoleInfoRepository.findByUsername(username);
        if(userRoleInfos==null || userRoleInfos.isEmpty()) throw new RuntimeException("User " + username + " does not found");
        UserRoleInfo userRoleInfo = userRoleInfos.get(0);
        List<String> roles = userRoleInfos.stream().map(UserRoleInfo::getRole).collect(Collectors.toList());
        return new UserRoleInfoDTO(userRoleInfo.getId(), userRoleInfo.getUsername(), roles);
    }

    @Override
    public Boolean registerUser(UserDetail userDetail) {
        log.info("register({})", userDetail.toString());
        Optional<User> fetchedUser = userRepository.findByUsername(userDetail.getUsername());
        if(fetchedUser.isPresent()) throw new RuntimeException("User " + userDetail.getUsername() + " is existed");
        if(userDetail.getUsername()==null) throw new RuntimeException("Username is null");
        if(userDetail.getPassword()==null) throw new RuntimeException("Password is null");

        List<String> roles = new ArrayList<>();
        roles.add(RoleConstant.USER.name());
        userDetail.setRoles(roles);
        return assignRoles(userDetail);
    }

    private Boolean assignRoles(UserDetail userDetail){
        try {
            List<Role> roles = roleService.getRoles(userDetail.getRoles());
            if(roles.size()!=userDetail.getRoles().size()) throw new RuntimeException("Invalid Role");
            List<Integer> roleIDs = roles.stream().map(Role::getId).collect(Collectors.toList());
            String hashedPassword = passwordEncoder.encode(userDetail.getPassword());
            String roleIDsText = objectMapper.writeValueAsString(roleIDs);
            userRoleInfoRepository.addUserDetail(userDetail.getUsername(), hashedPassword, roleIDsText);
            return true;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public Boolean addNewUser(UserDetail userDetail) {
        log.info("register({})", userDetail.toString());
        Optional<User> fetchedUser = userRepository.findByUsername(userDetail.getUsername());
        if(fetchedUser.isPresent()) throw new RuntimeException("User " + userDetail.getUsername() + " is existed");
        if(userDetail.getUsername()==null) throw new RuntimeException("Username is null");
        if(userDetail.getPassword()==null) throw new RuntimeException("Password is null");
        if(userDetail.getRoles()==null || userDetail.getRoles().isEmpty()) throw new RuntimeException("Roles is null");

        return this.assignRoles(userDetail);
    }
    
}