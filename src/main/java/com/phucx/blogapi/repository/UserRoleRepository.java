package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.phucx.blogapi.model.UserRole;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole>{

}
