package com.phucx.blogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.phucx.blogapi.model.Role;

import java.util.List;
import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByRole(String role);
    @Query("""
        SELECT r FROM Role r WHERE role IN ?1
            """)
    List<Role> findAllByRoleNames(List<String> roleName);
}
