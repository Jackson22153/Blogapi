package com.phucx.blogapi.service.user;

import com.phucx.blogapi.model.User;
import com.phucx.blogapi.model.UserDetail;
import com.phucx.blogapi.model.UserRoleInfoDTO;

public interface UserService {
    public Boolean authenticate(String username, String password);
    public User getUser(int id);
    public User getUserByUsername(String username);
    public Integer getUserID(String username);
    public UserRoleInfoDTO getUserRoles(String username);
    public Boolean registerUser(UserDetail userDetail);
    public Boolean addNewUser(UserDetail userDetail);
}
