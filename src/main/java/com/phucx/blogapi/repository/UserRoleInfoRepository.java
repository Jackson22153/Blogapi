package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.phucx.blogapi.compositeKey.UserRoleID;
import com.phucx.blogapi.model.UserRoleInfo;
import java.util.List;


@Repository
public interface UserRoleInfoRepository extends JpaRepository<UserRoleInfo, UserRoleID>{
    List<UserRoleInfo> findByUsername(String username);
    
    @Modifying
    @Transactional
    @Procedure("addUserDetail")
    void addUserDetail(String username, String password, String roleIDs_text);
}
